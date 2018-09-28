package myapplication.eye_found;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class BillboardActivity extends AppCompatActivity {

    public static final String url_data_billboard = "http://eyefoundapp.esy.es/eyefound/databillboard.php";
    public static final String idmediakit = "id_mk";
    public static final String side_code = "code_mk";
    public static final String location = "lokasi";
    public static final String city_names = "nama";
    public static final String views = "view";
    public static final String media_type = "tipe_media";
    public static final String types_light = "tipe_light";
    public static final String files_pdf = "file_pdf";
    public static final String photo = "foto";
    public static final String position = "posisi";
    public static final String width = "lebar";
    public static final String height = "tinggi";
    public static final String cnp1m = "np1";
    public static final String cnp3m = "np3";
    public static final String cnp6m = "np6";
    public static final String cnp12m = "np12";
    private RecyclerView recyclerView;
    private RecyclerViewAdapters RVAdapter;
    List<ListBillboard> billboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billboard);

        recyclerView = findViewById(R.id.listBillboard);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getBillboard();
    }

    private void getBillboard() {
        StringRequest stringRequest = new StringRequest(url_data_billboard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());

                billboardList = new ArrayList<ListBillboard>();

                String id_mediakit, sidecode, loc, cityname, view, mediatype, filepdf, type_light, photos, orientation, sizewidth, sizeheight, np1m, np3m, np6m, np12m;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        id_mediakit = jsonObject1.getString(idmediakit);
                        sidecode = jsonObject1.getString(side_code);
                        loc = jsonObject1.getString(location);
                        cityname = jsonObject1.getString(city_names);
                        view = jsonObject1.getString(views);
                        mediatype = jsonObject1.getString(media_type);
                        type_light = jsonObject1.getString(types_light);
                        filepdf = jsonObject1.getString(files_pdf);
                        photos = jsonObject1.getString(photo);
                        orientation = jsonObject1.getString(position);
                        sizewidth = jsonObject1.getString(width);
                        sizeheight = jsonObject1.getString(height);
                        np1m = jsonObject1.getString(cnp1m);
                        np3m = jsonObject1.getString(cnp3m);
                        np6m = jsonObject1.getString(cnp6m);
                        np12m = jsonObject1.getString(cnp12m);

                        billboardList.add(new ListBillboard(id_mediakit, sidecode, loc, cityname, view, mediatype, type_light, filepdf, photos, orientation, sizewidth, sizeheight, np1m, np3m, np6m, np12m));
                    }

                    RVAdapter = new RecyclerViewAdapters(billboardList);
                    recyclerView.setAdapter(RVAdapter);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BillboardActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
