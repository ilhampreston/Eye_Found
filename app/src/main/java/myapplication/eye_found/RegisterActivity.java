package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullnameet, notelpet;
    private Button btnReg;
    private TextView login;
    private String url_checkregist = "http://eyefoundapp.esy.es/eyefound/checkdataregister.php";
    private CountryCodePicker countryCodePicker;
    ConnectivityManager connectivityManager;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        fullnameet = findViewById(R.id.fullName);
        notelpet = findViewById(R.id.telpNum);
        btnReg = findViewById(R.id.BtnReg);
        login = findViewById(R.id.textView15);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        countryCodePicker.registerCarrierNumberEditText(notelpet);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (connectivityManager.getActiveNetworkInfo() != null
                    && connectivityManager.getActiveNetworkInfo().isAvailable()
                    && connectivityManager.getActiveNetworkInfo().isConnected()){
            }
            else {
                Toast.makeText(getApplicationContext(), "No Network Connection"
                        , Toast.LENGTH_LONG).show();
            }
        }

        awesomeValidation.addValidation(this,R.id.telpNum, "^8([0-9 -]{11,15})$", R.string.error_telp);
        awesomeValidation.addValidation(this,R.id.fullName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_telp);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
    private void Register(){
        final String fullname = this.fullnameet.getText().toString().trim();
        final String notelp = this.notelpet.getText().toString().trim();
        final String notelpccp = this.countryCodePicker.getFullNumberWithPlus();

        if (fullname.isEmpty()){
            fullnameet.setError("Full Name is empty");
        }
        else if (notelp.isEmpty()){
            notelpet.setError("Phone number is empty");
        }
        else {
            if (awesomeValidation.validate()) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_checkregist, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(RegisterActivity.this, PhoneAuthRegisterActivity.class);
                                intent.putExtra("fullname",fullname);
                                intent.putExtra("phone", notelpccp);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("fullname", fullname);
                        params.put("notelp", notelpccp);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }

            /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url_regist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Register Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("fullname", fullname);
                    params.put("notelp", notelp);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);*/

        }
    }
}
