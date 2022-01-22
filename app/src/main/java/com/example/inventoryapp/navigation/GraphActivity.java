package com.example.inventoryapp.navigation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventoryapp.R;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GraphActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    BarChart barChart;
    Toolbar toolbar;
    BarData barData;
    BarDataSet barDataSet,barDataSet2;
    public static int[] total = new int[12];
    public static ArrayList<BarEntry> entries;
    public static ArrayList<BarEntry> barEntries;
    String GraphData = "https://gravid-incentive.000webhostapp.com/Phpfyp/graph.php";
    String totalSales="https://gravid-incentive.000webhostapp.com/Phpfyp/totalSales.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Initialize();

        //System.out.println(total[8]);
    }

    public void Initialize() {
        barEntries = new ArrayList<>();
        entries = new ArrayList<>();
        barChart = (BarChart) findViewById(R.id.barChart);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        graphData();
        sellsData();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(11);
        xAxis.setGranularityEnabled(true);
//        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(months);
//        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(formatter);
//        barDataSet = new BarDataSet(getData(), "Inducesmile");
//        barDataSet.setBarBorderWidth(0.9f);
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        barData = new BarData(barDataSet);
//        barChart.setData(barData);
//        barChart.notifyDataSetChanged();
//        barChart.setVisibleXRangeMaximum(5);
        barChart.setFitBars(true);
        barChart.animateXY(5000, 5000);
//        graphData();
//        System.out.println(total[8]);
        //barChart.clearValues();
        //barChart.setViewPortOffsets(100f, 50f, 0f, 100f);
    }

    private void setGraph() {
        final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        barDataSet = new BarDataSet(barEntries, "Total Purchase");
        barDataSet2=new BarDataSet(entries,"Total Sales");
        barData = new BarData(barDataSet,barDataSet2);
        barChart.setData(barData);
        barDataSet.setColors(Color.rgb(0,128,0));
        barDataSet2.setColors(Color.rgb(128,128,0));
        barChart.zoom(2, 1, 2, 1);
        barDataSet.setBarBorderWidth(0.9f);
        barData.setBarWidth(0.6f);
        barData.setValueTextSize(10f);
        barChart.getAxisRight().setEnabled(false);
        barChart.enableScroll();
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList getData() {
        if ((entries) != null) {
            entries.clear();
            for (int i = 0; i < total.length; i++) {
                entries.add(new BarEntry(i, total[i]));
            }
        } else {
            for (int i = 0; i < total.length; i++) {
                entries.add(new BarEntry(i, total[i]));
            }
        }
        return entries;
    }


    public void graphData() {
        StringRequest request = new StringRequest(Request.Method.POST, GraphData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Sum");
                    int totalPurchase;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        //month=jsonObject1.getInt("month");
                        totalPurchase = jsonObject1.getInt("Total Purchase");
                        total[i] = totalPurchase;
                    }


                    for (int i = 0; i < total.length; i++) {
                        barEntries.add(new BarEntry(i, total[i]));
                        Log.d("myGraphData", "onResponse: " + total[i]);
                        Log.d("myGraphData", "onResponse: " + barEntries.size());
                    }
                    setGraph();
                    barChart.notifyDataSetChanged();

                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.getStackTraceString(ex);
                    Toast.makeText(GraphActivity.this, "Bad response from the server" + ex, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    Toast.makeText(GraphActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(GraphActivity.this, "Connection timed out for total purchase data", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(GraphActivity.this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GraphActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    public void sellsData(){
            StringRequest request = new StringRequest(Request.Method.POST,totalSales, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Sum");
                        int totalPurchase;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //month=jsonObject1.getInt("month");
                            totalPurchase = jsonObject1.getInt("Total Sales");
                            total[i] = totalPurchase;
                        }


                        for (int i = 0; i < total.length; i++) {
                            entries.add(new BarEntry(i, total[i]));
                            Log.d("myGraphData", "onResponse: " + total[i]);
                            Log.d("myGraphData", "onResponse: " + entries.size());
                        }
                        setGraph();
                        barChart.notifyDataSetChanged();

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        Log.getStackTraceString(ex);
                        Toast.makeText(GraphActivity.this, "Bad response from the server" + ex, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof ServerError) {
                        Toast.makeText(GraphActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(GraphActivity.this, "Connection timed out for total sales data", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof NetworkError) {
                        Toast.makeText(GraphActivity.this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GraphActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            request.setShouldCache(false);
            requestQueue.add(request);

    }

}