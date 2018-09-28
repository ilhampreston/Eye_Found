package myapplication.eye_found;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myapplication.eye_found.app.AppController;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String Id_Province = "code_p";
    public static final String Id_City = "code_kab";
    public static final String Id_Mediakit = "code_mk";
    public static final String Id_Media = "code_m";
    public static final String Id_Light = "code_l";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String Province_Name = "provinsi";
    public static final String Location = "lokasi";
    public static final String Orientation = "posisi";
    public static final String View_Details = "view";
    public static final String City_Name = "nama";
    public static final String Media_Type = "tipe_media";
    public static final String homePref = "detailpref";
    public static final String json_array = "result";
    public static final String url_marker = "http://eyefoundapp.esy.es/eyefound/data.php";
    private String location_detail;
    private SlideUp slideUp;
    private View Filtermaps;
    private View LayoutsFilter;
    private ArrayList<Marker> markerList;
    private FloatingActionButton BtnFilter, BtnSlideBack;
    private JSONArray result;
    private GoogleMap mMap;
    private AutoCompleteTextView autoCompleteTextView;
    private Button Search;
    Marker markers, markersearch;
    String id_city = "";
    String id_media = "";
    String media_name = "";
    LatLng latLng;
    Spinner mediatype, datacity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MarkerOptions markerOptions = new MarkerOptions();
    private String url_province = "http://eyefoundapp.esy.es/eyefound/dataprovince.php";
    private String url_city = "http://eyefoundapp.esy.es/eyefound/datacity.php?provinsi=";
    private String url_media = "http://eyefoundapp.esy.es/eyefound/datamediatype.php";
    private String url_marker_where = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere.php?code_m=";
    private String url_marker_where2 = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere2.php?code_m=";
    private String url_marker_where3 = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere3.php?code_mk=";
    ArrayList <String> provincelist;
    ArrayList <String> citylist;
    ArrayList <String> idcity;
    ArrayList <String> idmedia;
    ArrayList <String> idprovince;
    ArrayList <String> medialist;
    ArrayList <Province> listprovince;
    ArrayList <City> listcity;
    ArrayList <Media> listmedia;

    String[] cobaprovinsi =  {"DKI Jakarta", "Jawa Barat", "Jawa Timur", "Sumatera Utara", "Kalimantan Barat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        sharedPreferences = getSharedPreferences(homePref, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mediatype = findViewById(R.id.MediaList);
        datacity = findViewById(R.id.SearchCity);

        LayoutsFilter = findViewById(R.id.LayoutFilter);

        Filtermaps = findViewById(R.id.filtermaps);
        BtnFilter =  findViewById(R.id.BtnFilter);
        BtnSlideBack = findViewById(R.id.BtnSlideBack);

        BtnSlideBack.hide();

        slideUp = new SlideUpBuilder(LayoutsFilter).withListeners(new SlideUp.Listener.Events() {
            @Override
            public void onSlide(float percent) {
                Filtermaps.setAlpha(1 - (percent / 100));
                if (percent < 100 && BtnFilter.isShown()){
                    BtnFilter.hide();
                    BtnSlideBack.show();
                }
            }

            @Override
            public void onVisibilityChanged(int visibility) {
                if (visibility == View.GONE){
                    BtnFilter.show();
                    BtnSlideBack.hide();
                }

            }
        }).withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(findViewById(R.id.LayoutHome))
                .build();

        BtnSlideBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.hide();
            }
        });
        BtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.show();
            }
        });


        Search = (findViewById(R.id.search));
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.hide();
                closeKeyboard();
                filter();

            }
        });

        autoCompleteTextView = findViewById(R.id.SearchProvince);
        provincelist = new ArrayList<String>();
        idprovince = new ArrayList<String>();
        idcity = new ArrayList<String>();
        idmedia = new ArrayList<String>();
        citylist = new ArrayList<String>();
        medialist = new ArrayList<String>();
        listprovince = new ArrayList<Province>();
        listcity = new ArrayList<City>();
        listmedia = new ArrayList<Media>();
        markerList = new ArrayList<Marker>();

        getProvince();
        getMediaType();


        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.media_array, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mediatype.setAdapter(adapter);


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

    private void getProvince() {
        StringRequest stringRequest = new StringRequest(url_province, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    result = jsonObject.getJSONArray(json_array);
                    getDataProvince(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Log.e("Error :", error.getMessage());
               Toast.makeText(HomeActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDataProvince(JSONArray result) {
        for(int i = 0; i < result.length();i++){
            try {
                Province province = new Province();
                JSONObject json = result.getJSONObject(i);

                String provinceName = json.getString(Province_Name); //Deklarasi provinceName dalam bentuk String dengan data Province_Name
                String idProvince = json.getString(Id_Province); //Deklarasi idProvince dalam bentuk String dengan data Id_Province

                provincelist.add(provinceName); //Menampung hasil provinceName ke Array List provincelist
                idprovince.add(idProvince); //Menampung hasil idProvince ke Array List idProvince

                province.setIdProvince(idProvince); //Set Id Province dengan Kode Provinsi pada Database ke dalam Class Province
                province.setProvinceName(provinceName); //Set Nama Provinsi dengan Nama Provinsi pada Database ke dalam Class Province

                listprovince.add(province); // Menampung hasil class Province ke dalam Array List

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout,provincelist));
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout,provincelist));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String provincename = null;
                provincename = autoCompleteTextView.getText().toString().trim();

                for (Province province : listprovince ) {
                    if (province.provinceName.equals(provincename)) {
                        String id_province = province.getIdProvince();
                        break;
                    }
                }

                //Toast.makeText(HomeActivity.this, provincename, Toast.LENGTH_LONG).show();
                try {
                    getKabupaten(provincename);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                
            }


        });

    }


    private void getKabupaten(String provincename) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(provincename, "UTF-8");
        String url_wherecity = url_city+encode;

        StringRequest stringRequest = new StringRequest(url_wherecity, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    result = jsonObject.getJSONArray(json_array);
                    getDataCity(result);
                } catch (JSONException e) {
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


    private void getDataCity(JSONArray result) {
        citylist.clear();
        for(int i = 0; i < result.length();i++){
            try {
                City city = new City();
                JSONObject json = result.getJSONObject(i);

                String idCity = json.getString(Id_City);
                String cityName = json.getString(City_Name);

                idcity.add(idCity);
                citylist.add(cityName);

                city.setIdCity(idCity);
                city.setCityName(cityName);

                listcity.add(city);

            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
        datacity.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout, citylist));

        datacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                closeKeyboard();
                String cityname = null;
                cityname = datacity.getSelectedItem().toString();

                for (City city : listcity ) {
                    if (city.cityName.equals(cityname)) {
                        id_city = city.getIdCity();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void getMediaType() {
        StringRequest stringRequest = new StringRequest(url_media, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    result = jsonObject.getJSONArray(json_array);
                    getDataMedia(result);
                } catch (JSONException e) {
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

    private void getDataMedia(JSONArray result) {
        medialist.clear();
        for (int i = 0; i < result.length(); i++){
            try {
                Media media = new Media();
                JSONObject jsonObject = result.getJSONObject(i);

                String idMedia = jsonObject.getString(Id_Media);
                String mediaName = jsonObject.getString(Media_Type);

                idmedia.add(idMedia);
                medialist.add(mediaName);

                media.setIdMedia(idMedia);
                media.setMediaName(mediaName);

                listmedia.add(media);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        mediatype.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_layout,medialist));
        mediatype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                closeKeyboard();
                String mediaType = null;
                mediaType = mediatype.getSelectedItem().toString();

                for (Media media : listmedia){
                    if (media.mediaName.equals(mediaType)){
                        id_media = media.getIdMedia();
                        media_name = media.getMediaName();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                        String codemk = jsonObject1.getString(Id_Mediakit);
                        String locations = jsonObject1.getString(Location);

                        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                        markerOptions.position(latLng).title(codemk).snippet(locations);
                        markers = mMap.addMarker(markerOptions);

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                View view =  getLayoutInflater().inflate(R.layout.custom_info_window, null);

                                TextView code_mk = view.findViewById(R.id.code_mk);
                                TextView location = view.findViewById(R.id.location);

                                String codeMK = marker.getTitle();
                                //codeMK = marker.getTitle();
                                code_mk.setText(codeMK);
                                location.setText(marker.getSnippet());

                                getDetailInfoWindow(codeMK);

                                return view;
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", error.getMessage());
                Toast.makeText(HomeActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDetailInfoWindow(final String codeMK) {
        String url_info_window = url_marker_where3 + codeMK;
        StringRequest stringRequest = new StringRequest(url_info_window, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        //String lat = jsonObject1.getString(latitude);
                        //String lang = jsonObject1.getString(longitude);
                        final String codekab = jsonObject1.getString(Id_City);
                        final String codemk = jsonObject1.getString(Id_Mediakit);
                        final String locations = jsonObject1.getString(Location);
                        final String viewdetail = jsonObject1.getString(View_Details);
                        final String codemedia = jsonObject1.getString(Id_Media);
                        final String codelight = jsonObject1.getString(Id_Light);
                        final String orientation = jsonObject1.getString(Orientation);

                        //latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                editor.clear();
                                editor.commit();
                                editor.putString(Id_City,codekab);
                                editor.putString(Id_Mediakit,codemk);
                                editor.putString(Location, locations);
                                editor.putString(View_Details, viewdetail);
                                editor.putString(Id_Media,codemedia);
                                editor.putString(Id_Light,codelight);
                                editor.putString(Orientation,orientation);
                                editor.apply();
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e) {
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


    private void filter() {
        if(id_media != null && markers != null && id_city == ""){
            mMap.clear();
            String url_marker_media = url_marker_where + id_media;
            //Toast.makeText(getApplicationContext(),url_marker_media,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(url_marker_media, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        result = jsonObject.getJSONArray(json_array);
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject1 = result.getJSONObject(i);
                            String lat = jsonObject1.getString(latitude);
                            String lang = jsonObject1.getString(longitude);

                            final String codemk = jsonObject1.getString(Id_Mediakit);
                            final String locations = jsonObject1.getString(Location);

                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                            markerOptions.position(latLng).title(codemk).snippet(locations);
                            markersearch = mMap.addMarker(markerOptions);

                            gotoLocationZooms(latLng,13);

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                                    TextView code_mk = view.findViewById(R.id.code_mk);
                                    TextView location = view.findViewById(R.id.location);

                                    code_mk.setText(marker.getTitle());
                                    location.setText(marker.getSnippet());
                                    return view;
                                }
                            });

                        }
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
        else if(id_media != null && markers != null && id_city != ""){
            mMap.clear();
            String url_marker_media = url_marker_where2 + id_media + "&code_kab=" + id_city;
            //Toast.makeText(getApplicationContext(),url_marker_media,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(url_marker_media, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        result = jsonObject.getJSONArray(json_array);
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject1 = result.getJSONObject(i);
                            String lat = jsonObject1.getString(latitude);
                            String lang = jsonObject1.getString(longitude);

                            final String codemk = jsonObject1.getString(Id_Mediakit);
                            final String locations = jsonObject1.getString(Location);

                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                            markerOptions.position(latLng).title(codemk).snippet(locations);
                            markersearch = mMap.addMarker(markerOptions);

                            gotoLocationZooms(latLng,13);

                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {
                                    View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                                    TextView code_mk = view.findViewById(R.id.code_mk);
                                    TextView location = view.findViewById(R.id.location);

                                    code_mk.setText(marker.getTitle());
                                    location.setText(marker.getSnippet());
                                    return view;
                                }
                            });

                        }
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

    private void gotoLocationZooms(LatLng latLng, int i) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,i);
        mMap.animateCamera(cameraUpdate);
    }

    private void gotoLocationZoom(double lat, double lng, float i) {
        LatLng latLngEye = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngEye,i);
        mMap.animateCamera(cameraUpdate);
    }

    private void setMarkers(LatLng latLng, String codemk, String location) {

        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.eye_mark));

    }
    /*public void geoLocate(View view){
        AutoCompleteTextView searchProvince = findViewById(R.id.SearchProvince);
        String province = searchProvince.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        try{
            List<Address>addressList = geocoder.getFromLocationName(province,1);
            String string = addressList.get(0).getCountryName();
            Address address = addressList.get(0);
            Toast.makeText(this, string, Toast.LENGTH_LONG).show();
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            gotoLocationZoom(lat,lng,15);

            setMarker(string, lat, lng);
            //mMap.addMarker(new MarkerOptions().position(latLng).title(string));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setMarker(String string, double lat, double lng) {
        if (marker != null){
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(string)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.eye_mark))
                .position(new LatLng(lat,lng))
                .snippet("I am Here");
        marker = mMap.addMarker(markerOptions);
    }*/




}
