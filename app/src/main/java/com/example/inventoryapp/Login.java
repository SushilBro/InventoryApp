package com.example.inventoryapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private ImageButton btRegister;
    private TextView tvLogin;
    EditText Email,password;
    private static final String KEY_EMPTY = "";
    ProgressDialog pDialog;
    Button Login1;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String UserEmail,password1;
    RequestQueue requestQueue;
    private static final String REGISTER_REQUEST_URL = "https://gravid-incentive.000webhostapp.com/Phpfyp/login.php";
   // String loginUrl = "http://192.168.10.66:80/FYP/login.php";

    //String loginUrl = "http://sushilgautam.com.np/fyp/FYP/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btRegister  = findViewById(R.id.btRegister);
        Login1=(Button) findViewById(R.id.login);
        Email=(EditText)findViewById(R.id.userNameEmail);
        password=(EditText)findViewById(R.id.password);
        tvLogin     = findViewById(R.id.tvLogin);
        requestQueue = Volley.newRequestQueue(this);
        btnOnClick();
        //sharedPreferences = getSharedPreferences("inventoryapp", MODE_PRIVATE);
        //editpreferance = sharedPreferences.edit();
       // Email.setText(sharedPreferences.getString("email", ""));
       // password.setText(sharedPreferences.getString("password", ""));

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    public void btnOnClick(){
        btRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(Login.this, SignUp.class);
                Pair[] pairs    = new Pair[1];
                pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(intent,activityOptions.toBundle());
            }
        });
        Login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (validateInputs()){
                        displayLoader();
                        loadData();
                        pDialog.dismiss();
                    }
                }catch (Exception ex){

                }

            }
        });

    }
    private void displayLoader() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        if(Login.this != null && !Login.this.isFinishing()) {
            pDialog.show();
        }
    }
    public void loadData(){
                displayLoader();
                UserEmail=Email.getText().toString();
                password1=password.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_REQUEST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int json = jsonObject.getInt("success");
                    //String json=jsonObject.getString("success");
                    if (json==1) {
                        pDialog.dismiss();
                        Intent intent = new Intent(Login.this, HomePage.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Login.this, "login Succesfully", Toast.LENGTH_SHORT).show();
                    } else if(json==0)  {
                        pDialog.dismiss();
                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.getStackTraceString(ex);
                    Toast.makeText(Login.this, "Bad response" + ex, Toast.LENGTH_SHORT).show();
                }

            }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayLoader();
                if (error instanceof ServerError) {
                    Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Login.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(Login.this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("email", UserEmail);
                    parameters.put("password", password1);
                return parameters;
            }
            };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
        }
    private boolean validateInputs(){
        String email=Email.getText().toString().trim();
        if (KEY_EMPTY.equals(email)) {
            Email.setError("Email cannot be empty");
            Email.requestFocus();
            return false;
        }
        if (!email.matches(emailPattern)){
            Email.setError("please enter valid email");
            Email.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void finish()
    {
        if (pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }


    }


////                    OpenDatabase();
//                    Cursor cursor=sqLiteDatabase.rawQuery("select * from UserName where email=? and password=?",new String[]{UserEmail,password1});
//                    int count=cursor.getCount();
//                    if (count>0){

//                        while (cursor.moveToNext()) {
//                            if(checkBox1.isChecked()){
//                                checkBox1.setChecked(true);
//                                editpreferance.putString("password", cursor.getString(4));
//                                editpreferance.putString("email", cursor.getString(3));
//                                editpreferance.commit();
//
//                            }
//                            else {
//                                checkBox1.setChecked(false);
//                                editpreferance.clear();
//                                editpreferance.commit();
//                            }
//
//                        }
//                    }
//                    else {
//                        Toast.makeText(Login.this, "Username or password didn't match ", Toast.LENGTH_SHORT).show();
//                    }

//                    CloseDatabase();


//    public void OpenDatabase(){
//        DataBaseHelper dataBaseHelper=new DataBaseHelper(this);
//        sqLiteDatabase=dataBaseHelper.getWritableDatabase();
//    }
//    public void CloseDatabase(){
//        sqLiteDatabase.close();
//    }

