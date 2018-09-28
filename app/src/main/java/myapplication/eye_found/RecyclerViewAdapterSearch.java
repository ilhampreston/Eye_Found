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

import java.util.List;

public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerViewAdapterSearch.SearchViewHolder> {
    private List<ListSearch> searchList;
    private Context context;

    public static class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView sidecode;
        TextView location;
        TextView city;
        ImageView photo;
        Button btnDetail;

        public SearchViewHolder(final View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView5);
            sidecode = itemView.findViewById(R.id.textView6);
            location = itemView.findViewById(R.id.textView8);
            city = itemView.findViewById(R.id.billboard);
            btnDetail = itemView.findViewById(R.id.button3);
        }
    }

    public RecyclerViewAdapterSearch(List<ListSearch> searchList){
        this.searchList = searchList;
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public RecyclerViewAdapterSearch.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_billboards, parent, false);
        final RecyclerViewAdapterSearch.SearchViewHolder searchViewHolder = new RecyclerViewAdapterSearch.SearchViewHolder(view);
        searchViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("id_mediakit", searchList.get(searchViewHolder.getAdapterPosition()).getIdmediakit());
                intent.putExtra("sidecode", searchList.get(searchViewHolder.getAdapterPosition()).getSidecode());
                intent.putExtra("location", searchList.get(searchViewHolder.getAdapterPosition()).getLocation());
                intent.putExtra("city", searchList.get(searchViewHolder.getAdapterPosition()).getCity());
                intent.putExtra("view", searchList.get(searchViewHolder.getAdapterPosition()).getViewvalue());
                intent.putExtra("mediatype", searchList.get(searchViewHolder.getAdapterPosition()).getMediatype());
                intent.putExtra("tipe_light", searchList.get(searchViewHolder.getAdapterPosition()).getType_light());
                intent.putExtra("filepdf", searchList.get(searchViewHolder.getAdapterPosition()).getFilepdf());
                intent.putExtra("photo", searchList.get(searchViewHolder.getAdapterPosition()).getPhoto());
                intent.putExtra("orientation", searchList.get(searchViewHolder.getAdapterPosition()).getOrientation());
                intent.putExtra("sizewidth", searchList.get(searchViewHolder.getAdapterPosition()).getSizewidth());
                intent.putExtra("sizeheight", searchList.get(searchViewHolder.getAdapterPosition()).getSizeheight());
                view.getContext().startActivity(intent);
            }
        });
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterSearch.SearchViewHolder holder, int position) {
        holder.sidecode.setText(searchList.get(position).getSidecode());
        holder.location.setText(searchList.get(position).getLocation());
        holder.city.setText(searchList.get(position).getCity());
        Glide.with(holder.photo.getContext()).load("http://eyefoundapp.esy.es/assets/gambar/" + searchList.get(position).getPhoto()).into(holder.photo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
