package myapplication.eye_found;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideotronActivity extends AppCompatActivity {

    public static final String url_data_videotron = "http://eyefoundapp.esy.es/eyefound/datavideotron.php";
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
    private RecyclerView recyclerView;
    private RecyclerViewAdapterVideotron RVAdapterVideotron;
    List<ListVideotron> videotronList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videotron);

        recyclerView = findViewById(R.id.listVideotron);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getVideotron();
    }

    private void getVideotron() {
        StringRequest stringRequest = new StringRequest(url_data_videotron, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());

                videotronList = new ArrayList<ListVideotron>();

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


                        videotronList.add(new ListVideotron(id_mediakit, sidecode, loc, cityname, view, mediatype, type_light, filepdf, photos, orientation, sizewidth, sizeheight));
                    }

                    RVAdapterVideotron = new RecyclerViewAdapterVideotron(videotronList);
                    recyclerView.setAdapter(RVAdapterVideotron);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VideotronActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
