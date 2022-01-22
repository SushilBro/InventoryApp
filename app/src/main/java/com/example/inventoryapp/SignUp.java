package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    Button SignUP;
    TextView GoToLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SQLiteDatabase sqLiteDatabase;

    final ArrayList<String> emailPassword= new ArrayList<>();
    private static final String TAG_Message = "success";
    private static final String KEY_Email = "email";
    private static final String KEY_PhoneNumber = "phoneNumber";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private ProgressDialog pDialog;
    public static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    int value;
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String phoneNumber;
    String registerUrl = "https://gravid-incentive.000webhostapp.com/Phpfyp/register.php";
    RequestQueue requestQueue;
    private RelativeLayout rlayout;
    private Animation animation;
    EditText FullName,PhoneNumber,Email,Password,ConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        SignUP= findViewById(R.id.btnSignUp);
        GoToLogin=findViewById(R.id.btnLogin);
        FullName=(EditText)findViewById(R.id.fullName);
        PhoneNumber=(EditText)findViewById(R.id.phoneNumber);
        Email=(EditText)findViewById(R.id.email);
        Password=(EditText)findViewById(R.id.password1);
        ConfirmPassword=(EditText)findViewById(R.id.confirmPassword);
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        buttonClicked();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void buttonClicked(){
        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(validateInputs()){
                      regiser();
                    }

                }


        });
        GoToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });
    }
    private boolean validateInputs(){
        fullName=FullName.getText().toString().trim();
        phoneNumber=PhoneNumber.getText().toString().trim();
        email=Email.getText().toString().trim();
        password=Password.getText().toString().trim();
        confirmPassword=ConfirmPassword.getText().toString().trim();
        if (KEY_EMPTY.equals(email)) {
            Email.setError("email cannot be empty");
            Email.requestFocus();
            return false;
        }
        if (!email.matches(emailPattern)){
            Email.setError("please enter valid email");
            Email.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(fullName)) {
            FullName.setError("Full Name cannot be empty");
            FullName.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(phoneNumber)) {
            PhoneNumber.setError("Phone Number cannot be empty");
            PhoneNumber.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(password)) {
            Password.setError("Password cannot be empty");
            Password.requestFocus();
            return false;
        }
        if (password.length()<8){
            Password.setError("password cannot be smaller than 8 digits");
            Password.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            ConfirmPassword.setError("Confirm Password cannot be empty");
            ConfirmPassword.requestFocus();
            return false;
        }
        if (!confirmPassword.equals(password)) {
            ConfirmPassword.setError("Password and Confirm Password does not match");
            ConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(SignUp.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }
    private void regiser() {
        displayLoader();
        StringRequest request = new StringRequest(Request.Method.POST, registerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    value=jsonObject.getInt(TAG_Message);
                    //JSONArray jsonObject1=jsonObject.getJSONArray("success");
                    if (value==1){
                        pDialog.dismiss();
                        FullName.setText("");
                        PhoneNumber.setText("");
                        Email.setText("");
                        Password.setText("");
                        ConfirmPassword.setText("");
                        Toast.makeText(SignUp.this, "Succesfully registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(value==0){
                            pDialog.dismiss();
                            Toast.makeText(SignUp.this, "This email has already account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (JSONException ex){
                    ex.printStackTrace();
                    Log.getStackTraceString(ex);
                    Toast.makeText(SignUp.this, "Bad response from ther server"+ex, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayLoader();
                if (error instanceof ServerError) {
                    Toast.makeText(SignUp.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SignUp.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(SignUp.this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SignUp.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id", "");
                parameters.put("fullName", FullName.getText().toString());
                parameters.put("phoneNumber", PhoneNumber.getText().toString());
                parameters.put("email", Email.getText().toString());
                parameters.put("password", Password.getText().toString());
                //parameters.put("confirmPassword", ConfirmPassword.getText().toString());
                return parameters;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }
}

