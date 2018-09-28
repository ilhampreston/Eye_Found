package myapplication.eye_found;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String url_marker_where = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere.php?code_m=";
    private String url_marker_where2 = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere2.php?code_m=";
    private String url_marker_where4 = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere4.php?code_m=";
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
    private TextView jalan;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterSearch RVAdapterSearch;
    List<ListSearch> searchList;
    String id_media = "";
    String id_city = "";
    String id_jalan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.resultView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        jalan = findViewById(R.id.textViewjalan);


        id_media = getIntent().getExtras().getString("idmedia");
        id_city = getIntent().getExtras().getString("idcity");
        id_jalan = getIntent().getExtras().getString("idjalan");

        jalan.setText(id_jalan);


        if((id_media != null ) && (id_city.equals("")) && (id_jalan.equals(""))){
            String url_marker_media = url_marker_where + id_media;
            //Toast.makeText(getApplicationContext(),url_marker_media,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(url_marker_media, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response :", response.toString());
                    searchList = new ArrayList<ListSearch>();
                    String id_mediakit, sidecode, loc, cityname, view, mediatype, filepdf, type_light, photos, orientation, sizewidth, sizeheight;
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

                            searchList.add(new ListSearch(id_mediakit, sidecode, loc, cityname, view, mediatype, type_light, filepdf, photos, orientation, sizewidth, sizeheight));
                        }
                        RVAdapterSearch = new RecyclerViewAdapterSearch(searchList);
                        recyclerView.setAdapter(RVAdapterSearch);

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else if(id_media != null && id_city.equals("") && id_jalan != null){
            String url_marker_media = url_marker_where4 + id_media + "&id_mk=" + id_jalan;
            //Toast.makeText(getApplicationContext(),url_marker_media,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(url_marker_media, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response :", response.toString());
                    searchList = new ArrayList<ListSearch>();
                    String id_mediakit, sidecode, loc, cityname, view, mediatype, filepdf, type_light, photos, orientation, sizewidth, sizeheight;
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

                            searchList.add(new ListSearch(id_mediakit, sidecode, loc, cityname, view, mediatype, type_light, filepdf, photos, orientation, sizewidth, sizeheight));
                        }
                        RVAdapterSearch = new RecyclerViewAdapterSearch(searchList);
                        recyclerView.setAdapter(RVAdapterSearch);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}
