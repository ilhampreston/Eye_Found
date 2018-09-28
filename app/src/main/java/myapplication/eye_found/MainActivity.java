package myapplication.eye_found;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.print.PrinterId;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String url_order = "http://eyefoundapp.esy.es/eyefound/order.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading....");
        progressDialog.setCancelable(false);

        SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String iduser = sharedPreferences.getString(SessionManager.ID_SHARED_PREF, "");
        String orientationvalue = null;

        final String idmedia = getIntent().getExtras().getString("id_mediakit");
        String sidecodenum = getIntent().getExtras().getString("sidecode");
        String locationtext = getIntent().getExtras().getString("location");
        String citytext = getIntent().getExtras().getString("city");
        String view = getIntent().getExtras().getString("view");
        String mediatype = getIntent().getExtras().getString("mediatype");
        String tipe_light = getIntent().getExtras().getString("tipe_light");
        final String filepdf = getIntent().getExtras().getString("filepdf");
        final String photo = getIntent().getExtras().getString("photo");
        final String URLPhoto = "http://eyefoundapp.esy.es/assets/gambar/" + photo;
        final String URLPdf = "http://eyefoundapp.esy.es/assets/pdf/" + filepdf;
        String orientation = getIntent().getExtras().getString("orientation");
        String sizewidth = getIntent().getExtras().getString("sizewidth");
        String sizeheight = getIntent().getExtras().getString("sizeheight");
        String cnp1m = getIntent().getExtras().getString("np1m");
        String cnp3m = getIntent().getExtras().getString("np3m");
        String cnp6m = getIntent().getExtras().getString("np6m");
        String cnp12m = getIntent().getExtras().getString("np12m");

        Double dcnp1m = Double.parseDouble(cnp1m);
        Double dcnp3m = Double.parseDouble(cnp3m);
        Double dcnp6m = Double.parseDouble(cnp6m);
        Double dcnp12m = Double.parseDouble(cnp12m);

        Locale localeID = new Locale("in", "ID");

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);


        //TextView provinceinfo = findViewById(R.id.provinceInfo);
        TextView title = findViewById(R.id.TitleInfo);
        //TextView sidecode = findViewById(R.id.sideCodeNum);
        TextView viewdetails = findViewById(R.id.viewValue);
        TextView location = findViewById(R.id.locationValue);
        TextView size = findViewById(R.id.sizeValue);
        //TextView mediatypevalue = findViewById(R.id.mediatypeValue);
        TextView lighttype = findViewById(R.id.lighttypeValue);
        TextView orientationtext = findViewById(R.id.orientationValue);
        TextView np1mvalue = findViewById(R.id.price1monthValue);
        TextView np3mvalue = findViewById(R.id.price3monthValue);
        TextView np6mvalue = findViewById(R.id.price6monthValue);
        TextView np12mvalue = findViewById(R.id.price12monthValue);
        ImageView photos = findViewById(R.id.imageView7);
        Button btnDownload = findViewById(R.id.button2);
        Button btnOrder = findViewById(R.id.button6);
        
        if (orientation.equals("Vr")){
            orientationvalue = "Vertical";
        }
        else if (orientation.equals("Hr")){
            orientationvalue = "Horizontal";
        }

        title.setText("Detail " + mediatype);
        //sidecode.setText(sidecodenum);
        location.setText(locationtext);
        //provinceinfo.setText(citytext);
        size.setText(sizewidth + "m x " + sizeheight + "m");
        viewdetails.setText(view);
        lighttype.setText(tipe_light);
        Glide.with(getApplicationContext()).load(URLPhoto).into(photos);
        orientationtext.setText(orientationvalue);
        np1mvalue.setText(numberFormat.format((double) dcnp1m));
        np3mvalue.setText(numberFormat.format((double) dcnp3m));
        np6mvalue.setText(numberFormat.format((double) dcnp6m));
        np12mvalue.setText(numberFormat.format((double) dcnp12m));
       //mediatypevalue.setText(mediatype);


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PDFDownload(MainActivity.this, URLPdf);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_order, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Intent intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Order success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Order error, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("iduser", iduser);
                        params.put("id_mk", idmedia);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

                //Toast.makeText(MainActivity.this, "Order success", Toast.LENGTH_SHORT).show();

            }
        });

    }
    /*
    private class downloadPDF extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File folder =  new File(extStorageDirectory);

            File pdffile = new File(folder,fileName);

            try{
                pdffile.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            DownloadPDF.downloadFile(fileUrl,pdffile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hidepDialog();
            Toast.makeText(getApplicationContext(), "Download PDf successfully", Toast.LENGTH_SHORT).show();
            Log.d("Download complete", "----------");
        }
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }*/
}
