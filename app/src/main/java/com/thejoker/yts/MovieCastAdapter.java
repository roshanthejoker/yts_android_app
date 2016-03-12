package com.thejoker.yts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.MovieCastViewHolder> {
    private ArrayList<MovieCastDetails> castMovies = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private ClickListener clickListener;



    public MovieCastAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setCastMovies(ArrayList<MovieCastDetails> castMovies) {
        this.castMovies = castMovies;
        notifyItemRangeChanged(0, castMovies.size());

    }

    public class MovieCastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView actorImage;
        private TextView actorName;

        public MovieCastViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            actorImage = (ImageView) itemView.findViewById(R.id.actor_image);
            actorName = (TextView) itemView.findViewById(R.id.actor_name);
        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());

            }

        }
    }

    @Override
    public MovieCastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cast_recyclerview_item, parent, false);
        MovieCastViewHolder viewHolder = new MovieCastViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieCastViewHolder holder, int position) {
        holder.itemView.setTag(castMovies.get(position));
        holder.actorName.setText(castMovies.get(position).getName());
        String url = castMovies.get(position).getImageUrl();
        Glide.with(context)
                .load(url)
                .override(60, 60)
                .placeholder(R.drawable.ic_default_pic)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.actorImage);
    }


    @Override
    public int getItemCount() {
        return castMovies.size();
    }

    public void setClickListener(ClickListener clickListener) {

        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}
