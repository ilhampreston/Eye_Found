package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText phone;
    private String url_login = "http://eyefoundapp.esy.es/eyefound/login.php";
    private AwesomeValidation awesomeValidation;
    ConnectivityManager connectivityManager;
    CountryCodePicker countryCodePicker;
    private Boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phonenum);
        countryCodePicker = findViewById(R.id.countryCodePicker2);
        countryCodePicker.registerCarrierNumberEditText(phone);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

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

        awesomeValidation.addValidation(this,R.id.phonenum, "^8([0-9 -]{11,15})$", R.string.error_telp);
        Button btnLogin = findViewById(R.id.BtnLogin);
        TextView register = findViewById(R.id.textView14);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mPhone = countryCodePicker.getFullNumberWithPlus();
                
                if (mPhone.isEmpty()){
                    phone.setError("Phone number is empty");
                }
                else {
                    if (awesomeValidation.validate()) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");

                                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                                    if (success.equals("1")) {
                                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        for (int i = 0; i < jsonArray.length(); i++){
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String fullname = object.getString("fullname").trim();
                                            String phone = object.getString("notelp").trim();
                                            String iduser = object.getString("iduser").trim();

                                            editor.putBoolean(SessionManager.LOGGEDIN_SHARED_PREF_NAME, true);
                                            editor.putString(SessionManager.NAME_SHARED_PREF, fullname);
                                            editor.putString(SessionManager.PHONE_SHARED_PREF, phone);
                                            editor.putString(SessionManager.ID_SHARED_PREF, iduser);
                                            editor.commit();

                                            Intent intent = new Intent(LoginActivity.this, PhoneAuthLoginActivity.class);
                                            intent.putExtra("phone", mPhone);
                                            startActivity(intent);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "Your number not registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("notelp", mPhone);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                    }

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(SessionManager.LOGGEDIN_SHARED_PREF_NAME,false);

        if (loggedIn){
            Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
            startActivity(intent);
        }
    }

}
