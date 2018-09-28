package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PhoneAuthLoginActivity extends AppCompatActivity {

    private TextView phonenumber, timertext, resendcode;
    private String url_login = "http://eyefoundapp.esy.es/eyefound/login.php";
    private String mobile, fullname;
    //private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private EditText codeet;
    private Button btnVerify;
    private Timer timer;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private Boolean mVerified = false;
    private Boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_login);


        phonenumber = findViewById(R.id.textView17);
        timertext = findViewById(R.id.textView20);
        //resendcode = findViewById(R.id.textView21);
        mAuth = FirebaseAuth.getInstance();
        codeet = findViewById(R.id.editText);
        btnVerify = findViewById(R.id.button);

        FirebaseApp.initializeApp(this);
        Intent intent = getIntent();

        mobile = intent.getStringExtra("phone");

        phonenumber.setText(mobile);

        starttimer();
        startPhoneNumberVerification(mobile);


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codenum = codeet.getText().toString().trim();
                if (codenum.isEmpty() || codenum.length() < 6 || codenum.length() > 6) {
                    codeet.setError("Enter valid code");
                    codeet.requestFocus();
                    return;
                }
                verifyVerificationCode(codenum);
                //starttimer();
                /*if (btnVerify.getTag().equals("send")){
                    if (!phonenumber.getText().toString().trim().isEmpty() && phonenumber.getText().toString().trim().length() >= 13 ){
                        startPhoneNumberVerification(phonenumber.getText().toString().trim());
                        mVerified = false;
                        starttimer();
                        btnVerify.setTag("verify");
                    }
                    else {
                        phonenumber.setError("Please enter valid mobile number");
                    }
                }
                if (btnVerify.getTag().equals("verify")) {
                    if (!code.getText().toString().trim().isEmpty() && !mVerified) {
                        Snackbar snackbar = Snackbar
                                .make((ConstraintLayout) findViewById(R.id.layoutConstraint), "Please wait...", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, code.getText().toString().trim());
                        signInWithPhoneAuthCredential(credential);
                    }
                    if (mVerified) {
                        startActivity(new Intent(PhoneAuthRegisterActivity.this, LoginActivity.class));
                    }
                }*/
            }

        });



        timertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phonenumber.getText().toString().trim().isEmpty() && phonenumber.getText().toString().trim().length() >= 13) {
                    resendVerificationCode(phonenumber.getText().toString().trim(), mResendToken);
                    mVerified = false;
                    //starttimer();
                    btnVerify.setTag("verify");
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.layoutConstraintLogin), "Resending verification code...", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    //@Override
    /*protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(SessionManager.LOGGEDIN_SHARED_PREF_NAME,false);

        if (loggedIn){
            Intent intent = new Intent(PhoneAuthLoginActivity.this, BottomNavigationActivity.class);
            startActivity(intent);
        }
    }*/

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String smscode = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (smscode != null) {
                codeet.setText(smscode);
                //verifying the code
                verifyVerificationCode(smscode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("TAG", "onVerificationFailed: ", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException){
                Toast.makeText(PhoneAuthLoginActivity.this,"Verification Failed !! Invalid verification Code", Toast.LENGTH_LONG).show();
            }
            else if (e instanceof FirebaseTooManyRequestsException){
                Toast.makeText(PhoneAuthLoginActivity.this,"Verification Failed !! Too many request. Try after some time. ", Toast.LENGTH_LONG).show();
            }


        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
            mResendToken = forceResendingToken;

        }
    };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).
                addOnCompleteListener(PhoneAuthLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        JSONArray jsonArray = jsonObject.getJSONArray("login");

                                        if (success.equals("1")) {
                                            //SharedPreferences sharedPreferences = PhoneAuthLoginActivity.this.getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                            //SharedPreferences.Editor editor = sharedPreferences.edit();
                                            for (int i = 0; i < jsonArray.length(); i++){
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                String fullname = object.getString("fullname").trim();
                                                String phone = object.getString("notelp").trim();
                                                String iduser = object.getString("iduser").trim();

                                                /*editor.putBoolean(SessionManager.LOGGEDIN_SHARED_PREF_NAME, true);
                                                editor.putString(SessionManager.NAME_SHARED_PREF, fullname);
                                                editor.putString(SessionManager.PHONE_SHARED_PREF, phone);
                                                editor.putString(SessionManager.ID_SHARED_PREF, iduser);
                                                editor.commit();*/

                                                Intent intent = new Intent(PhoneAuthLoginActivity.this, BottomNavigationActivity.class);
                                                //intent.putExtra("fullname", fullname);
                                                //intent.putExtra("phone", phone);
                                                //intent.putExtra("iduser", iduser);
                                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                        /*else{
                                            Toast.makeText(PhoneAuthLoginActivity.this,
                                                    "Success login. \nYour Phone Number : "
                                                            +phonenumber, Toast.LENGTH_SHORT).show();
                                        }*/
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(PhoneAuthLoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(PhoneAuthLoginActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("notelp", mobile);
                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);

                            //verification successful we will start the profile activity

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutConstraint), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                /*if (task.isSuccessful()){
                    Log.d("TAG", "signInWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    mVerified = true;
                    timer.cancel();
                    timertext.setVisibility(View.INVISIBLE);
                    resendcode.setVisibility(View.INVISIBLE);
                    code.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar
                            .make((ConstraintLayout) findViewById(R.id.layoutConstraint), "Successfully Verified", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }*/
                /*else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Snackbar snackbar = Snackbar
                                .make((ConstraintLayout) findViewById(R.id.layoutConstraint), "Invalid OTP ! Please enter correct OTP", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }*/
                    }
                });
    }

    public void starttimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            int second = 60;

            @Override
            public void run() {
                if (second <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timertext.setText("Resend Code");
                            timer.cancel();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timertext.setText("00:" + second--);
                        }
                    });
                }

            }
        }, 0, 1000);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    /*private void Login(final String mPhone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String fullname = object.getString("fullname").trim();
                            String phone = object.getString("notelp").trim();
                            String iduser = object.getString("iduser").trim();

                            //Toast.makeText(LoginActivity.this,
                            //  "Success login. \nYour Full Name : "
                            //    +fullname+ "\n Your Phone : "
                            //     +phone, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                            intent.putExtra("fullname", fullname);
                            intent.putExtra("phone", phone);
                            intent.putExtra("iduser", iduser);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this,
                                "Success login. \nYour Phone : "
                                        +phone, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}
