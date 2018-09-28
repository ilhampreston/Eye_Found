package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.List;

public class RecyclerViewAdapters extends RecyclerView.Adapter<RecyclerViewAdapters.BillboardViewHolder> {

    private List<ListBillboard> billboardList;
    private Context context;
    private String photos, URLPhoto;

    public static class BillboardViewHolder extends RecyclerView.ViewHolder{

        TextView sidecode;
        TextView location;
        TextView city;
        ImageView photo;
        Button btnDetail;

        public BillboardViewHolder(final View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView5);
            sidecode = itemView.findViewById(R.id.textView6);
            location = itemView.findViewById(R.id.textView8);
            city = itemView.findViewById(R.id.billboard);
            btnDetail = itemView.findViewById(R.id.button3);
        }
    }

    public RecyclerViewAdapters(List<ListBillboard> billboardList){
        this.billboardList = billboardList;
    }

    @Override
    public int getItemCount() {
        return billboardList.size();
    }

    @Override
    public BillboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_billboards, parent, false);
        final BillboardViewHolder billboardViewHolder = new BillboardViewHolder(view);
        billboardViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("id_mediakit", billboardList.get(billboardViewHolder.getAdapterPosition()).getIdmediakit());
                intent.putExtra("sidecode", billboardList.get(billboardViewHolder.getAdapterPosition()).getSidecode());
                intent.putExtra("location", billboardList.get(billboardViewHolder.getAdapterPosition()).getLocation());
                intent.putExtra("city", billboardList.get(billboardViewHolder.getAdapterPosition()).getCity());
                intent.putExtra("view", billboardList.get(billboardViewHolder.getAdapterPosition()).getViewvalue());
                intent.putExtra("mediatype", billboardList.get(billboardViewHolder.getAdapterPosition()).getMediatype());
                intent.putExtra("tipe_light", billboardList.get(billboardViewHolder.getAdapterPosition()).getType_light());
                intent.putExtra("filepdf", billboardList.get(billboardViewHolder.getAdapterPosition()).getFilepdf());
                intent.putExtra("photo", billboardList.get(billboardViewHolder.getAdapterPosition()).getPhoto());
                intent.putExtra("orientation", billboardList.get(billboardViewHolder.getAdapterPosition()).getOrientation());
                intent.putExtra("sizewidth", billboardList.get(billboardViewHolder.getAdapterPosition()).getSizewidth());
                intent.putExtra("sizeheight", billboardList.get(billboardViewHolder.getAdapterPosition()).getSizeheight());
                intent.putExtra("np1m", billboardList.get(billboardViewHolder.getAdapterPosition()).getNp1m());
                intent.putExtra("np3m", billboardList.get(billboardViewHolder.getAdapterPosition()).getNp3m());
                intent.putExtra("np6m", billboardList.get(billboardViewHolder.getAdapterPosition()).getNp6m());
                intent.putExtra("np12m", billboardList.get(billboardViewHolder.getAdapterPosition()).getNp12m());
                view.getContext().startActivity(intent);
            }
        });
        return billboardViewHolder;
    }

    @Override
    public void onBindViewHolder(BillboardViewHolder holder, int position) {
        holder.sidecode.setText(billboardList.get(position).getSidecode());
        holder.location.setText(billboardList.get(position).getLocation());
        holder.city.setText(billboardList.get(position).getCity());
        Glide.with(holder.photo.getContext())
                .load("http://eyefoundapp.esy.es/assets/gambar/" + billboardList.get(position).getPhoto())
                .into(holder.photo);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
