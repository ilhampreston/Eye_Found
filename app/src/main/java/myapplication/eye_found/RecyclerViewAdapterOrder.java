package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapterOrder extends RecyclerView.Adapter<RecyclerViewAdapterOrder.OrderViewHolder> {
    private List<ListOrder> orderList;
    private Context context;
    private String photos, URLPhoto;
    private String url_cancel = "http://eyefoundapp.esy.es/eyefound/deleteorder.php?idpesan=";

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView sidecode;
        TextView location;
        TextView city;
        Button btnDetail, btnCancel;

        public OrderViewHolder(final View itemView) {
            super(itemView);
            sidecode = itemView.findViewById(R.id.textView6);
            location = itemView.findViewById(R.id.textView8);
            city = itemView.findViewById(R.id.billboard);
            btnDetail = itemView.findViewById(R.id.button3);
            btnCancel = itemView.findViewById(R.id.button5);
        }
    }
    public RecyclerViewAdapterOrder(List<ListOrder> orderList){
        this.orderList = orderList;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public RecyclerViewAdapterOrder.OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order, parent, false);
        final RecyclerViewAdapterOrder.OrderViewHolder orderViewHolder = new RecyclerViewAdapterOrder.OrderViewHolder(view);
        orderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("id_mediakit", orderList.get(orderViewHolder.getAdapterPosition()).getIdmediakit());
                intent.putExtra("sidecode", orderList.get(orderViewHolder.getAdapterPosition()).getSidecode());
                intent.putExtra("location", orderList.get(orderViewHolder.getAdapterPosition()).getLocation());
                intent.putExtra("city", orderList.get(orderViewHolder.getAdapterPosition()).getCity());
                intent.putExtra("view", orderList.get(orderViewHolder.getAdapterPosition()).getViewvalue());
                intent.putExtra("mediatype", orderList.get(orderViewHolder.getAdapterPosition()).getMediatype());
                intent.putExtra("tipe_light", orderList.get(orderViewHolder.getAdapterPosition()).getType_light());
                intent.putExtra("filepdf", orderList.get(orderViewHolder.getAdapterPosition()).getFilepdf());
                intent.putExtra("photo", orderList.get(orderViewHolder.getAdapterPosition()).getPhoto());
                intent.putExtra("orientation", orderList.get(orderViewHolder.getAdapterPosition()).getOrientation());
                intent.putExtra("sizewidth", orderList.get(orderViewHolder.getAdapterPosition()).getSizewidth());
                intent.putExtra("sizeheight", orderList.get(orderViewHolder.getAdapterPosition()).getSizeheight());
                view.getContext().startActivity(intent);
            }
        });
        orderViewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String idpesan = orderList.get(orderViewHolder.getAdapterPosition()).getIdpesan();
                String urlOrdercancel = url_cancel + idpesan;

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, urlOrdercancel, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response :", response.toString());
                        Toast.makeText(view.getContext(),"Cancel order success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(view.getContext(), BottomNavigationActivity.class);
                        view.getContext().startActivity(intent);
                        //Toast.makeText(this, "Cancel Order Success", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error :", error.toString());
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                requestQueue.add(stringRequest);
            }
        });
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterOrder.OrderViewHolder holder, int position) {
        holder.sidecode.setText(orderList.get(position).getSidecode());
        holder.location.setText(orderList.get(position).getLocation());
        holder.city.setText(orderList.get(position).getCity());
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
