package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventoryapp.avilable_goods.model.AvaialbleGoodsModel;
import com.example.inventoryapp.avilable_goods.model.AvailableListModel;
import com.example.inventoryapp.avilable_goods.model.AvilableGoods;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.SelectionKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SellProduct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ImageView btnBack;
    EditText Quantity,SellingPrice,CustomerName,CustomerAddress,CustomerPhoneNumber,sellingDate;
    Button SellProduct;
    private static final String KEY_EMPTY = "";
    private static final String KEY_ZERO = "0";
    RequestQueue requestQueue;
    Toolbar toolbar;
    //boolean checkSold;
    private Animation animation;
    private RelativeLayout rlayout;
    AutoCompleteTextView ProductName;
    String sellProductUrl="https://gravid-incentive.000webhostapp.com/Phpfyp/sellProducts.php";
    String AddSells="https://gravid-incentive.000webhostapp.com/Phpfyp/AddSells.php";
    String AddCustomer="https://gravid-incentive.000webhostapp.com/Phpfyp/insertCustomers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);
        ProductName=(AutoCompleteTextView) findViewById(R.id.productName);
        Quantity=(EditText)findViewById(R.id.quantity);
        sellingDate=findViewById(R.id.salesDate);
        sellingDate.setKeyListener(null);
        SellingPrice=(EditText)findViewById(R.id.sellingPrice);
        CustomerName=(EditText)findViewById(R.id.customerName);
        CustomerAddress=(EditText)findViewById(R.id.customerAddress);
        CustomerPhoneNumber=(EditText)findViewById(R.id.customerNumber);
        SellProduct=(Button)findViewById(R.id.btnSellProduct);
        requestQueue = Volley.newRequestQueue(this);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rlayout=findViewById(R.id.rlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        new AvilableGoods();
        Quantity.setText("0");
        SellingPrice.setText("0");
        //this.checkSold=false;


// Get the string array
        ArrayList product=new ArrayList<String>();
        for (int i=0;i<AvilableGoods.avaialbleGoodsModelList.size();i++){
            product.add(AvilableGoods.avaialbleGoodsModelList.get(i).getPrductName());
        }
// Create the adapter and set it to the AutoCompleteTextView
        new AvailableListModel();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, product);
        ProductName.setAdapter(adapter);
        buttonClicked();

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
    public void buttonClicked(){

        SellProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()){
                    sellProduct();
                   // if (checkSold){
                        insertSells();
                        insertCustomer();
                    //}
                }

            }
        });
        sellingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               datePicker();
            }
        });
    }
//    private boolean isCheckSold(){
//        return this.checkSold;
//    }
    private void datePicker(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    public void sellProduct(){
        StringRequest request = new StringRequest(Request.Method.POST, sellProductUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int json = jsonObject.getInt("success");
                    //String json=jsonObject.getString("success");
                    if (json==1) {
                        Toast.makeText(SellProduct.this, "Sold Successfully", Toast.LENGTH_SHORT).show();
                        //checkSold = true;
                    } else if(json==0)  {
                        Toast.makeText(SellProduct.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        //checkSold=false;
                    }
                    else {
                        Toast.makeText(SellProduct.this, "Error", Toast.LENGTH_SHORT).show();
                        //checkSold=false;
                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.getStackTraceString(ex);
                    Toast.makeText(SellProduct.this, "Bad response" + ex, Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(SellProduct.this, "Sucessfully Sold item", Toast.LENGTH_SHORT).show();
                ProductName.setText("");
                Quantity.setText(""+0);
                SellingPrice.setText(""+0);
                CustomerName.setText("");
                CustomerAddress.setText("");
                CustomerPhoneNumber.setText("");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {

                    Toast.makeText(SellProduct.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    System.out.println(error.getMessage());
                }

                else {
                    Toast.makeText(SellProduct.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters  = new HashMap<String, String>();
                String quantity=Quantity.getText().toString();
                int q=Integer.parseInt(quantity);
                String sellingPrice=SellingPrice.getText().toString();
                int price=Integer.parseInt(sellingPrice);
                parameters.put("customerName",CustomerAddress.getText().toString());
                parameters.put("customerAddress",CustomerAddress.getText().toString());
                parameters.put("customerPhoneNumeber",CustomerPhoneNumber.getText().toString());
                parameters.put("sellingPrice",String.valueOf(price));
                parameters.put("salesDate",sellingDate.getText().toString());
                parameters.put("productName",ProductName.getText().toString());
                parameters.put("quantity",String.valueOf(q));
                return checkParams(parameters);
            }
        };
        request.setShouldCache(false);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public void insertCustomer(){

            StringRequest request = new StringRequest(Request.Method.POST, AddCustomer, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() != null) {

                        Toast.makeText(SellProduct.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }

                    else {
                        Toast.makeText(SellProduct.this, "error", Toast.LENGTH_LONG).show();
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters  = new HashMap<String, String>();
                    String quantity=Quantity.getText().toString();
                    int q=Integer.parseInt(quantity);
                    String sellingPrice=SellingPrice.getText().toString();
                    int price=Integer.parseInt(sellingPrice);
                    parameters.put("customerName",CustomerName.getText().toString());
                    parameters.put("phoneNumber",CustomerPhoneNumber.getText().toString());
                    parameters.put("address",CustomerAddress.getText().toString());
                    parameters.put("quantity",String.valueOf(q));
                    parameters.put("productName",ProductName.getText().toString());
                    return checkParams(parameters);
                }
            };
            request.setShouldCache(false);
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);




    }
    public void insertSells(){
        StringRequest request = new StringRequest(Request.Method.POST, AddSells, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {

                    Toast.makeText(SellProduct.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    System.out.println(error.getMessage());
                }

                else {
                    Toast.makeText(SellProduct.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters  = new HashMap<String, String>();
                String quantity=Quantity.getText().toString();
                int q=Integer.parseInt(quantity);
                String sellingPrice=SellingPrice.getText().toString();
                int price=Integer.parseInt(sellingPrice);
                parameters.put("sellingPrice",String.valueOf(price));
                parameters.put("salesDate",sellingDate.getText().toString());
                parameters.put("productName",ProductName.getText().toString());
                parameters.put("quantity",String.valueOf(q));
                return checkParams(parameters);
            }
        };
        request.setShouldCache(false);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int finalMonth = month + 1;
        String date = year + "-" + finalMonth + "-" + dayOfMonth;
        sellingDate.setInputType(InputType.TYPE_NULL);
        sellingDate.requestFocus();
        sellingDate.setText(date);
    }
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    private boolean validation(){
        {
            String productName=ProductName.getText().toString().trim();
            String quantity=Quantity.getText().toString().trim();
            String sp=SellingPrice.getText().toString().trim();
            Calendar today = Calendar.getInstance();


            if (KEY_EMPTY.equals(productName)) {
                ProductName.setError("Product Name cannot be empty");
                ProductName.requestFocus();
                return false;
            }
            if (KEY_ZERO.equals(quantity)) {
                Quantity.setError("Please enter some quantity");
                Quantity.requestFocus();
                return false;
            }
            if (KEY_ZERO.equals(sp)) {
                SellingPrice.setError("Please Enter Selling Price");
                SellingPrice.requestFocus();
                return false;
            }
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String myDate=sellingDate.getText().toString();
                Date date = format.parse(myDate);
                Calendar finalDate=toCalendar(date);
                if (finalDate.after(today)){
                    Toast.makeText(this, "Your Date cannot be greater than today's date", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            catch (ParseException e){
                e.printStackTrace();
            }

            return true;
        }
    }
}
