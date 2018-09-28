package myapplication.eye_found;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    private String url_order = "http://eyefoundapp.esy.es/eyefound/dataorder.php?iduser=";
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterOrder RVAdapterOrder;
    private ImageView orderimage;
    private TextView youhavent, ordernow;
    private String iduser;
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
    public static final String id_order = "idpesan";
    List<ListOrder> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = view.findViewById(R.id.orderlist);
        //recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        iduser = sharedPreferences.getString(SessionManager.ID_SHARED_PREF, "");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //orderimage = view.findViewById(R.id.imageView14);
        //youhavent = view.findViewById(R.id.textView);
        //ordernow = view.findViewById(R.id.textView10);
        //orderimage.setVisibility(View.GONE);
        //youhavent.setVisibility(View.GONE);
        //ordernow.setVisibility(View.GONE);*/

        showData();

        return view;
    }

    private void showData() {
        String url_orderuser = url_order + iduser;
        StringRequest stringRequest = new StringRequest(url_orderuser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response :", response.toString());
                orderList = new ArrayList<ListOrder>();

                String id_mediakit, sidecode, loc, cityname, view, mediatype, filepdf, type_light, photos, orientation, sizewidth, sizeheight, idpesan;
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
                        idpesan = jsonObject1.getString(id_order);

                        orderList.add(new ListOrder(id_mediakit, sidecode, loc, cityname, view, mediatype, type_light, filepdf, photos, orientation, sizewidth, sizeheight, idpesan));
                    }
                    RVAdapterOrder = new RecyclerViewAdapterOrder(orderList);
                    recyclerView.setAdapter(RVAdapterOrder);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
