package myapplication.eye_found;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomesActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String url_marker = "http://eyefoundapp.esy.es/eyefound/data.php";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private Marker markers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homes);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerOptions = new MarkerOptions();
    }

    public void gotobillboard(View view) {
        Intent intent = new Intent(HomesActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        gotoLocationZoom(-6.2384812,106.7861935,10);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle2));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }
        getMarker();
    }

    private void getMarker() {
        StringRequest stringRequest = new StringRequest(url_marker, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String lat = jsonObject1.getString(latitude);
                        String lang = jsonObject1.getString(longitude);

                        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                        markerOptions.position(latLng);
                        markers = mMap.addMarker(markerOptions);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", error.getMessage());
                Toast.makeText(HomesActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void gotoLocationZoom(double lat, double lng, float i) {
        LatLng latLngEye = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngEye,i);
        mMap.animateCamera(cameraUpdate);
    }
}
