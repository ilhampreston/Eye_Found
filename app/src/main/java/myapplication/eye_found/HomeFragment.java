package myapplication.eye_found;

import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    public static final String url_marker = "http://eyefoundapp.esy.es/eyefound/data.php";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String code_m = "code_m";
    public static final String Id_Mediakit = "id_mk";
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
    private View view;
    private ConstraintLayout constraintBillboard, constraintDigitalBanner, constraintVideotron;
    private String url_marker_where3 = "http://eyefoundapp.esy.es/eyefound/datamarkerwhere3.php?code_mk=";
    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private Marker markers;
    private String codemk, sidecode, loc, cityname, viewmarker, mediatype, filepdf, type_light, photos, orientation, sizewidth, sizeheight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        constraintBillboard = view.findViewById(R.id.constraintLayout5);
        constraintBillboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),BillboardActivity.class);
                startActivity(intent);
            }
        });
        constraintDigitalBanner = view.findViewById(R.id.constraintLayout6);
        constraintDigitalBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),DigitalBannerActivity.class);
                startActivity(intent);
            }
        });
        constraintVideotron = view.findViewById(R.id.constraintLayout8);
        constraintVideotron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VideotronActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerOptions = new MarkerOptions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        gotoLocationZoom(-6.2384812,106.7861935,10);

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
                        //codemk = jsonObject1.getString(Id_Mediakit);
                        String codemedia = jsonObject1.getString(code_m);
                        sidecode = jsonObject1.getString(side_code);
                        loc = jsonObject1.getString(location);
                        //cityname = jsonObject1.getString(city_names);
                        //viewmarker = jsonObject1.getString(views);
                        //mediatype = jsonObject1.getString(media_type);
                        //type_light = jsonObject1.getString(types_light);
                        //filepdf = jsonObject1.getString(files_pdf);
                        //photos = jsonObject1.getString(photo);
                        //orientation = jsonObject1.getString(position);
                        //sizewidth = jsonObject1.getString(width);
                        //sizeheight = jsonObject1.getString(height);*/


                        if (codemedia.equals("01")){
                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                            markerOptions.position(latLng).title(sidecode).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).snippet(loc);
                            markers = mMap.addMarker(markerOptions);

                            //mMap.setOnMarkerClickListener(markers.getId());
                        }
                        else if (codemedia.equals("02")){
                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                            markerOptions.position(latLng).title(sidecode).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).snippet(loc);
                            markers = mMap.addMarker(markerOptions);
                        }
                        else if (codemedia.equals("10")){
                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                            markerOptions.position(latLng).title(sidecode).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet(loc);
                            markers = mMap.addMarker(markerOptions);
                        }
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

                                getDetailMarker(codeMK);

                                return view;
                            }
                        });
                        /*final HashMap<String, String> markerMap = new HashMap<String, String>();
                        final String id_mediakit = markers.getId();
                        markerMap.put(id_mediakit, codemk);
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                String idgethash = markerMap.get(marker.getId());

                                if (idgethash.equals(id_mediakit)){
                                    Intent intent = new Intent(getActivity(), MainActivity.class);

                                }

                            }
                        });*/
                        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                String codeMK = marker.getTitle();
                                Toast.makeText(getActivity(), codeMK, Toast.LENGTH_SHORT).show();
                                getDetailMarker(codeMK);
                                return true;
                            }
                        });*/
                        /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                String codeMK = marker.getTitle();
                                getDetailMarker(codeMK);
                                return null;
                            }
                        });*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", error.getMessage());
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void gotoLocationZoom(double lat, double lng, int i) {
        LatLng latLngEye = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngEye,i);
        mMap.animateCamera(cameraUpdate);
    }

    private void getDetailMarker(String codeMK) {
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
                        String lat = jsonObject1.getString(latitude);
                        String lang = jsonObject1.getString(longitude);
                        final String codemks = jsonObject1.getString(Id_Mediakit);
                        String codemedia = jsonObject1.getString(code_m);
                        final String sidecodes = jsonObject1.getString(side_code);
                        final String locs = jsonObject1.getString(location);
                        final String citynames = jsonObject1.getString(city_names);
                        final String viewmarkers = jsonObject1.getString(views);
                        final String mediatypes = jsonObject1.getString(media_type);
                        final String type_lights = jsonObject1.getString(types_light);
                        final String filepdfs = jsonObject1.getString(files_pdf);
                        final String photoss = jsonObject1.getString(photo);
                        final String orientations = jsonObject1.getString(position);
                        final String sizewidths = jsonObject1.getString(width);
                        final String sizeheights = jsonObject1.getString(height);
                        final String np1ms = jsonObject1.getString(cnp1m);
                        final String np3ms = jsonObject1.getString(cnp3m);
                        final String np6ms = jsonObject1.getString(cnp6m);
                        final String np12m = jsonObject1.getString(cnp12m);

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("id_mediakit", codemks);
                                intent.putExtra("sidecode", sidecodes);
                                intent.putExtra("location", locs);
                                intent.putExtra("city", citynames);
                                intent.putExtra("view", viewmarkers);
                                intent.putExtra("mediatype", mediatypes);
                                intent.putExtra("tipe_light", type_lights);
                                intent.putExtra("filepdf", filepdfs);
                                intent.putExtra("photo", photoss);
                                intent.putExtra("orientation", orientations);
                                intent.putExtra("sizewidth", sizewidths);
                                intent.putExtra("sizeheight", sizeheights);
                                intent.putExtra("np1m", np1ms);
                                intent.putExtra("np3m", np3ms);
                                intent.putExtra("np6m", np6ms);
                                intent.putExtra("np12m", np12m);
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
                Log.e("Error :", error.getMessage());
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
