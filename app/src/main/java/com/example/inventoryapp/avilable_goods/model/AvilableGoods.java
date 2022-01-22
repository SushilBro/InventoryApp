package com.example.inventoryapp.avilable_goods.model;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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
import com.example.inventoryapp.RecyclerViewModelAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AvilableGoods extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefereshlayout;
    private BroadcastReceiver broadcastReceiver;
    RequestQueue requestQueue;
    RecyclerViewModelAdapter adapterSupport;
    String productsData = "https://gravid-incentive.000webhostapp.com/Phpfyp/newSendValue.php";

    public static ArrayList<AvaialbleGoodsModel> avaialbleGoodsModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avilable_goods);
        recyclerView = findViewById(R.id.recycerview_available_goods);
        adapterSupport = new RecyclerViewModelAdapter(avaialbleGoodsModelList);
        recyclerView.setAdapter(adapterSupport);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        requestQueue = Volley.newRequestQueue(this);
        swipeRefereshlayout = findViewById(R.id.refreshView);
        swipeRefereshlayout.setOnRefreshListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);
        Products();

        adapterSupport.setOnItemClickListener(new RecyclerViewModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(AvilableGoods.this, "Clicked", Toast.LENGTH_SHORT).show();
                UpdateItem(position,"Fanta");
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public void onRefresh() {
            avaialbleGoodsModelList.clear();
            adapterSupport.notifyDataSetChanged();
            Products();
    }

    public void Products() {
        StringRequest request = new StringRequest(Request.Method.POST, productsData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (avaialbleGoodsModelList.size() == 0) {
                        if (response != null) {
                            Gson gson = new Gson();
                            AvailableListModel avaialbleGoodsModel = gson.fromJson(response, AvailableListModel.class);
                            avaialbleGoodsModelList.addAll(avaialbleGoodsModel.products);
                            int prevSize = avaialbleGoodsModelList.size();
                            adapterSupport.notifyItemRangeInserted(prevSize, avaialbleGoodsModelList.size() -prevSize);
                            adapterSupport.notifyDataSetChanged();
                        }
                        if (swipeRefereshlayout.isRefreshing()) {
                            swipeRefereshlayout.setRefreshing(false);
                            Toast.makeText(AvilableGoods.this, "Refresh successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        if (swipeRefereshlayout.isRefreshing()) {
                            swipeRefereshlayout.setRefreshing(false);
                            Toast.makeText(AvilableGoods.this, "Refresh successfully", Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.getStackTraceString(ex);
                    Toast.makeText(AvilableGoods.this, "Bad response from ther server" + ex, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof ServerError) {
                    Toast.makeText(AvilableGoods.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(AvilableGoods.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(AvilableGoods.this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AvilableGoods.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    public void UpdateItem(int position,String productName){
        avaialbleGoodsModelList.get(position).Update(productName);
        adapterSupport.notifyItemChanged(position);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput=newText.toLowerCase();
        List<AvaialbleGoodsModel> newList=new ArrayList<>();
        for(AvaialbleGoodsModel avaialbleGoodsModel : avaialbleGoodsModelList){
            if(avaialbleGoodsModel.getPrductName().toLowerCase().contains(userInput)){
                newList.add(avaialbleGoodsModel);
            }
        }
        adapterSupport.updateList(newList);
        return true;
    }
}
