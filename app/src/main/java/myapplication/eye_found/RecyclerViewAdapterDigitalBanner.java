package myapplication.eye_found;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class RecyclerViewAdapterDigitalBanner extends RecyclerView.Adapter<RecyclerViewAdapterDigitalBanner.DigitalBannerViewHolder> {

    private List<ListDigitalBanner> digitalBannerList;
    private Context context;

    public static class DigitalBannerViewHolder extends RecyclerView.ViewHolder{

        TextView sidecode;
        TextView location;
        TextView city;
        ImageView photo;
        Button btnDetail;

        public DigitalBannerViewHolder(final View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView5);
            sidecode = itemView.findViewById(R.id.textView6);
            location = itemView.findViewById(R.id.textView8);
            city = itemView.findViewById(R.id.billboard);
            btnDetail = itemView.findViewById(R.id.button3);
        }
    }

    public RecyclerViewAdapterDigitalBanner(List<ListDigitalBanner> digitalBannerList){
        this.digitalBannerList = digitalBannerList;
    }

    @Override
    public int getItemCount() {
        return digitalBannerList.size();
    }

    @Override
    public RecyclerViewAdapterDigitalBanner.DigitalBannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_billboards, parent, false);
        final RecyclerViewAdapterDigitalBanner.DigitalBannerViewHolder digitalBannerViewHolder = new RecyclerViewAdapterDigitalBanner.DigitalBannerViewHolder(view);
        digitalBannerViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("id_mediakit", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getIdmediakit());
                intent.putExtra("sidecode", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getSidecode());
                intent.putExtra("location", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getLocation());
                intent.putExtra("city", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getCity());
                intent.putExtra("view", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getViewvalue());
                intent.putExtra("mediatype", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getMediatype());
                intent.putExtra("tipe_light", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getType_light());
                intent.putExtra("filepdf", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getFilepdf());
                intent.putExtra("photo", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getPhoto());
                intent.putExtra("orientation", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getOrientation());
                intent.putExtra("sizewidth", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getSizewidth());
                intent.putExtra("sizeheight", digitalBannerList.get(digitalBannerViewHolder.getAdapterPosition()).getSizeheight());
                view.getContext().startActivity(intent);
            }
        });
        return digitalBannerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterDigitalBanner.DigitalBannerViewHolder holder, int position) {
        holder.sidecode.setText(digitalBannerList.get(position).getSidecode());
        holder.location.setText(digitalBannerList.get(position).getLocation());
        holder.city.setText(digitalBannerList.get(position).getCity());
        Glide.with(holder.photo.getContext())
                .load("http://eyefoundapp.esy.es/assets/gambar/" + digitalBannerList.get(position).getPhoto())
                .into(holder.photo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
