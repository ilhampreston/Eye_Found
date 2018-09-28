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

public class RecyclerViewAdapterVideotron extends RecyclerView.Adapter<RecyclerViewAdapterVideotron.VideotronViewHolder> {

    private List<ListVideotron> videotronList;
    private Context context;

    public static class VideotronViewHolder extends RecyclerView.ViewHolder{

        TextView sidecode;
        TextView location;
        TextView city;
        ImageView photo;
        Button btnDetail;

        public VideotronViewHolder(final View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imageView5);
            sidecode = itemView.findViewById(R.id.textView6);
            location = itemView.findViewById(R.id.textView8);
            city = itemView.findViewById(R.id.billboard);
            btnDetail = itemView.findViewById(R.id.button3);
        }
    }

    public RecyclerViewAdapterVideotron(List<ListVideotron> videotronList){
        this.videotronList = videotronList;
    }

    @Override
    public int getItemCount() {
        return videotronList.size();
    }

    @Override
    public RecyclerViewAdapterVideotron.VideotronViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_billboards, parent, false);
        final RecyclerViewAdapterVideotron.VideotronViewHolder videotronViewHolder = new RecyclerViewAdapterVideotron.VideotronViewHolder(view);
        videotronViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("id_mediakit", videotronList.get(videotronViewHolder.getAdapterPosition()).getIdmediakit());
                intent.putExtra("sidecode", videotronList.get(videotronViewHolder.getAdapterPosition()).getSidecode());
                intent.putExtra("location", videotronList.get(videotronViewHolder.getAdapterPosition()).getLocation());
                intent.putExtra("city", videotronList.get(videotronViewHolder.getAdapterPosition()).getCity());
                intent.putExtra("view", videotronList.get(videotronViewHolder.getAdapterPosition()).getViewvalue());
                intent.putExtra("mediatype", videotronList.get(videotronViewHolder.getAdapterPosition()).getMediatype());
                intent.putExtra("tipe_light", videotronList.get(videotronViewHolder.getAdapterPosition()).getType_light());
                intent.putExtra("filepdf", videotronList.get(videotronViewHolder.getAdapterPosition()).getFilepdf());
                intent.putExtra("photo", videotronList.get(videotronViewHolder.getAdapterPosition()).getPhoto());
                intent.putExtra("orientation", videotronList.get(videotronViewHolder.getAdapterPosition()).getOrientation());
                intent.putExtra("sizewidth", videotronList.get(videotronViewHolder.getAdapterPosition()).getSizewidth());
                intent.putExtra("sizeheight", videotronList.get(videotronViewHolder.getAdapterPosition()).getSizeheight());
                view.getContext().startActivity(intent);
            }
        });
        return videotronViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterVideotron.VideotronViewHolder holder, int position) {
        holder.sidecode.setText(videotronList.get(position).getSidecode());
        holder.location.setText(videotronList.get(position).getLocation());
        holder.city.setText(videotronList.get(position).getCity());
        Glide.with(holder.photo.getContext()).load("http://eyefoundapp.esy.es/assets/gambar/" + videotronList.get(position).getPhoto()).into(holder.photo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
