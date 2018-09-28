package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment{

    private String url_province = "http://eyefoundapp.esy.es/eyefound/dataprovince.php";
    private String url_city = "http://eyefoundapp.esy.es/eyefound/datacity.php?provinsi=";
    private String url_media = "http://eyefoundapp.esy.es/eyefound/datamediatype.php";
    private String url_jalan = "http://eyefoundapp.esy.es/eyefound/data.php";

    public static final String Id_Province = "code_p";
    public static final String Province_Name = "provinsi";
    public static final String Id_City = "code_kab";
    public static final String City_Name = "nama";
    public static final String Id_Media = "code_m";
    public static final String Media_Type = "tipe_media";
    public static final String Location = "lokasi";
    public static final String Id_Mediakit = "id_mk";
    private AutoCompleteTextView autoCompleteTextView;
    private EditText jalan;
    private Spinner spinnerCity, spinnerMedia;
    private Button btnSearch;
    private View view;
    String id_city = "";
    String id_media = "";
    String id_jalan = "";
    private String media_name = "";
    private ArrayList<String> idprovince, provincelist, idcity, citylist, idmedia, medialist, idmediakit, mediakitlist;
    private ArrayList<Province> listprovince;
    private ArrayList<Jalan> listjalan;
    private ArrayList<City> listcity;
    private ArrayList<Media> listmedia;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        
        view = inflater.inflate(R.layout.fragment_search, container, false);

        autoCompleteTextView = view.findViewById(R.id.SearchProvince);
        spinnerCity = view.findViewById(R.id.searchCity);
        spinnerMedia = view.findViewById(R.id.MediaList);

        jalan = view.findViewById(R.id.editText2);

        btnSearch = view.findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMediakit();
            }
        });

        getProvince();
        getMediaType();
        getJalan();

        return view;
        
        

    }

    private void getJalan() {
        StringRequest stringRequest = new StringRequest(url_jalan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response : ", response.toString());
                idmediakit = new ArrayList<String>();
                mediakitlist = new ArrayList<String>();
                listjalan = new ArrayList<Jalan>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Jalan jalan = new Jalan();

                        String lokasi = jsonObject1.getString(Location);
                        String id_mediakit = jsonObject1.getString(Id_Mediakit);

                        mediakitlist.add(lokasi);
                        idmediakit.add(id_mediakit);

                        jalan.setNamajalan(lokasi);
                        jalan.setIdMediakit(id_mediakit);

                        listjalan.add(jalan);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jalan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String streetname = null;
                        streetname = jalan.getText().toString().trim();

                        for (Jalan jalan : listjalan){
                            if (jalan.namajalan.equals(streetname)){
                                id_jalan = jalan.getIdMediakit();
                                break;
                            }
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getProvince() {
        StringRequest stringRequest = new StringRequest(url_province, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());
                idprovince = new ArrayList<String>();
                provincelist = new ArrayList<String>();
                listprovince = new ArrayList<Province>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for(int i = 0; i < jsonArray.length();i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Province province = new Province();

                            String provinceName = jsonObject1.getString(Province_Name); //Deklarasi provinceName dalam bentuk String dengan data Province_Name
                            String idProvince = jsonObject1.getString(Id_Province); //Deklarasi idProvince dalam bentuk String dengan data Id_Province

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
                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout,provincelist));
                    autoCompleteTextView.setThreshold(1);
                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout,provincelist));

                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            closeKeyboard();
                            String provincename = null;
                            provincename = autoCompleteTextView.getText().toString().trim();

                            for (Province province : listprovince ) {
                                if (province.provinceName.equals(provincename)) {
                                    String id_province = province.getIdProvince();
                                    break;
                                }
                            }
                            try {
                                getKabupaten(provincename);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getKabupaten(String provincename) throws UnsupportedEncodingException{
        String encode = URLEncoder.encode(provincename, "UTF-8");
        String url_wherecity = url_city+encode;

        StringRequest stringRequest = new StringRequest(url_wherecity, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());
                idcity = new ArrayList<String>();
                citylist = new ArrayList<String>();
                listcity = new ArrayList<City>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);

                    citylist.clear();
                    for(int i = 0; i < jsonArray.length();i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            City city = new City();

                            String idCity = jsonObject1.getString(Id_City);
                            String cityName = jsonObject1.getString(City_Name);

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
                    spinnerCity.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, citylist));

                    spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            closeKeyboard();
                            String cityname = null;
                            cityname = spinnerCity.getSelectedItem().toString();

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getMediaType() {
        StringRequest stringRequest = new StringRequest(url_media, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());
                idmedia = new ArrayList<String>();
                medialist = new ArrayList<String>();
                listmedia = new ArrayList<Media>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String getObject = jsonObject.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);
                    medialist.clear();
                    for (int i = 0; i < jsonArray.length(); i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Media media = new Media();

                            String idMedia = jsonObject1.getString(Id_Media);
                            String mediaName = jsonObject1.getString(Media_Type);

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
                    spinnerMedia.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout,medialist));
                    spinnerMedia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            closeKeyboard();
                            String mediaType = null;
                            mediaType = spinnerMedia.getSelectedItem().toString();

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void searchMediakit() {
        Intent intent = new Intent(view.getContext(), SearchActivity.class);
        intent.putExtra("idmedia", id_media);
        intent.putExtra("idcity", id_city);
        intent.putExtra("idjalan", id_jalan);
        view.getContext().startActivity(intent);
        /*if(id_media != null && id_city == ""){
            String url_marker_media = url_marker_where + id_media;
            //Toast.makeText(getApplicationContext(),url_marker_media,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(url_marker_media, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response :", response.toString());
                    String sidecodevalue, loc, cityname, viewvalue, mediatype, filepdf;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String getObject = jsonObject.getString("result");
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            sidecodevalue = jsonObject1.getString(side_code);
                            loc = jsonObject1.getString(location);
                            cityname = jsonObject1.getString(city_names);
                            viewvalue = jsonObject1.getString(views);
                            mediatype = jsonObject1.getString(media_type);
                            filepdf = jsonObject1.getString(files_pdf);

                            searchList.add(new ListSearch(sidecodevalue, loc, cityname, viewvalue, mediatype, filepdf));
                        }

                        Intent intent = new Intent(view.getContext(), SearchActivity.class);
                        view.getContext().startActivity(intent);

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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
        else if(id_media != null && id_city != ""){
            String url_marker_media = url_marker_where2 + id_media + "&code_kab=" + id_city;
            //Toast.makeText(getApplicationContext(),url_marker_media,Toast.LENGTH_LONG).show();
            StringRequest stringRequest = new StringRequest(url_marker_media, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response :", response.toString());
                    String sidecodevalue = null, loc = null, cityname = null, viewvalue = null, mediatype = null, filepdf = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String getObject = jsonObject.getString("result");
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            sidecodevalue = jsonObject1.getString(side_code);
                            loc = jsonObject1.getString(location);
                            cityname = jsonObject1.getString(city_names);
                            viewvalue = jsonObject1.getString(views);
                            mediatype = jsonObject1.getString(media_type);
                            filepdf = jsonObject1.getString(files_pdf);

                            searchList.add(new ListSearch(sidecodevalue, loc, cityname, viewvalue, mediatype, filepdf));
                        }
                        Intent intent = new Intent(view.getContext(), SearchActivity.class);
                        view.getContext().startActivity(intent);
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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }*/
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
