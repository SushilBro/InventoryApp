package com.example.inventoryapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class AddProduct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button selectImage,AddProduct;
    private ProgressDialog pDialog;
    private int checkEditText;
    Bitmap bitmap;
    final Calendar myCalendar = Calendar.getInstance();
    static final int REQUEST_TAKE_PHOTO = 123;
    ImageView productImage,openCamera;
    EditText ProductName,Quantity,PurchasePrice,SalesPrice,PurchaseDate,Description,mfdDate,expireDate;
    RequestQueue requestQueue;
    String image1;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String KEY_EMPTY = "";
    private static final String KEY_ZERO = 0+"";
    String insertUrl = "https://gravid-incentive.000webhostapp.com/Phpfyp/insertProducts.php";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        requestQueue = Volley.newRequestQueue(this);
        selectImage=(Button)findViewById(R.id.btnImage);
        AddProduct=(Button)findViewById(R.id.btnAddProduct);
        ProductName=(EditText)findViewById(R.id.productName);
        Quantity=(EditText)findViewById(R.id.quantity1);
        PurchasePrice=(EditText)findViewById(R.id.purchasePrice1);
        SalesPrice=(EditText)findViewById(R.id.salesPrice1);
        PurchaseDate=(EditText)findViewById(R.id.purchaseDate);
        PurchaseDate.setKeyListener(null);
        mfdDate=(EditText)findViewById(R.id.mfdDate);
        expireDate=(EditText)findViewById(R.id.expDate);
        expireDate.setKeyListener(null);
        PurchaseDate=(EditText)findViewById(R.id.purchaseDate);
        PurchaseDate.setKeyListener(null);
        Description=(EditText)findViewById(R.id.discription);
        productImage=(ImageView)findViewById(R.id.productImage);
        openCamera=findViewById(R.id.openCamera);
        buttonOnclick();
        PurchaseDate.setText(java.time.LocalDate.now().toString());

    }

    public void buttonOnclick(){
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

            }
        });
        selectImage.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), PICK_IMAGE_REQUEST);
            }
        }));

        PurchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                checkEditText=0;
            }
        });
        mfdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                checkEditText=1;
            }
        });
        expireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                checkEditText=2;
            }
        });
        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validInput()){
                    addproduct();
                }
            }

        });
    }
    private void datePicker(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void addproduct(){

                StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        displayLoader();
                        Toast.makeText(AddProduct.this, "Sucessfully Inserted", Toast.LENGTH_SHORT).show();
                        ProductName.setText("");
                        PurchaseDate.setText("");
                        Description.setText("");
                        Quantity.setText(""+0);
                        PurchasePrice.setText(""+0);
                        SalesPrice.setText(""+0);
                        mfdDate.setText("");
                        expireDate.setText("");
                        PurchaseDate.setText("");
                        pDialog.dismiss();
                        productImage.setImageDrawable(null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            displayLoader();
                            Toast.makeText(AddProduct.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage());
                        }

                        else {
                            Toast.makeText(AddProduct.this, "error", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> parameters  = new HashMap<String, String>();
                        String quantity=Quantity.getText().toString();
                        int q=Integer.parseInt(quantity);
                        String purchasePrice=PurchasePrice.getText().toString();
                        int pp=Integer.parseInt(purchasePrice);
                        String salesPrice=SalesPrice.getText().toString();
                        int sp=Integer.parseInt(salesPrice);
                        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.
                        byte [] byte_arr = stream.toByteArray();
                        String productImageS = Base64.encodeToString(byte_arr, Base64.DEFAULT);*/
                        parameters.put("porductId", "");
                        parameters.put("productName",ProductName.getText().toString());
                        parameters.put("quantity",String.valueOf(q));
                        parameters.put("purchasePrice",String.valueOf(pp));
                        parameters.put("salesPrice",String.valueOf(sp));
                        parameters.put("purchaseDate",PurchaseDate.getText().toString());
                        parameters.put("mfdDate",mfdDate.getText().toString());
                        parameters.put("expDate",expireDate.getText().toString());
                        parameters.put("note",Description.getText().toString());
                        parameters.put("productImage",image1);
                        return checkParams(parameters);
                    }
                    private Map<String, String> checkParams(Map<String, String> map){
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                            if(pairs.getValue()==null){
                                map.put(pairs.getKey(),"");
                            }
                        }
                        return map;
                    }
                    /*@Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        long imagename = System.currentTimeMillis();
                        params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                        return params;
                    }*/

                };


        request.setShouldCache(false);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                String image = getStringImage(lastBitmap);
                Log.d("image",image);
                productImage.setImageBitmap(lastBitmap);
                //passing the image to volley
                image1=image;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap lastBitmap = null;
            lastBitmap = photo;
            //encoding image to string
            String image = getStringImage(lastBitmap);
            Log.d("image",image);
            productImage.setImageBitmap(lastBitmap);
            //passing the image to volley
            image1=image;
        }
    }

    private boolean validInput(){
        String prodcutName=ProductName.getText().toString().trim();
        String quantity=Quantity.getText().toString().trim();
        String purchasePrice=PurchasePrice.getText().toString().trim();
        String salesPrice=SalesPrice.getText().toString().trim();
        String purhaseDate=PurchaseDate.getText().toString().trim();
        String mfd_Date=mfdDate.getText().toString().trim();
        String expDate=expireDate.getText().toString().trim();
        String discription=Description.getText().toString().trim();
        SellProduct sellProduct=new SellProduct();
        Calendar today = Calendar.getInstance();

        if (KEY_EMPTY.equals(prodcutName)) {
            ProductName.setError("Products cannot be empty");
            ProductName.requestFocus();
            return false;
        }
        if (KEY_ZERO.equals(quantity) || KEY_EMPTY.equals(quantity)) {
            Quantity.setError("Quantity cannot be zero or null");
            Quantity.requestFocus();
            return false;
        }
        if (KEY_ZERO.equals(purchasePrice) || KEY_EMPTY.equals(purchasePrice)) {
            PurchasePrice.setError("purchase price cannot be zero or null");
            PurchasePrice.requestFocus();
            return false;
        }
        if (KEY_ZERO.equals(salesPrice) || KEY_EMPTY.equals(salesPrice)) {
            SalesPrice.setError("Sales price cannot be zero or null");
            SalesPrice.requestFocus();
            return false;
        }
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(purhaseDate);
            Calendar finalDate=sellProduct.toCalendar(date);
            if (finalDate.after(today)) {
                Toast.makeText(this, "Your purchase date cannot be greater than today's date", Toast.LENGTH_SHORT).show();
                return false;
            }
            Date mfd=format.parse(mfd_Date);
            Calendar mfdDAte=sellProduct.toCalendar(mfd);
            if (mfdDAte.after(today)) {
                Toast.makeText(this, "Your manufacture date cannot be greater than today's date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        return true;

    }
    private void displayLoader() {
        pDialog = new ProgressDialog(AddProduct.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int finalMonth = month + 1;
        String date = year + "-" + finalMonth + "-" + dayOfMonth;
        if (checkEditText == 0) {
            PurchaseDate.setInputType(InputType.TYPE_NULL);
            PurchaseDate.requestFocus();
            PurchaseDate.setText(date);
        } else if (checkEditText == 1) {
            mfdDate.setInputType(InputType.TYPE_NULL);
            mfdDate.requestFocus();
            mfdDate.setText(date);
        } else if (checkEditText == 2) {
            expireDate.setInputType(InputType.TYPE_NULL);
            expireDate.requestFocus();
            expireDate.setText(date);
        }

    }


}

