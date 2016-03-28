package com.thejoker.yts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private ClickListener clickListener;
    private RealmResults<FavoriteListRealm> mResults;
    public FavoriteListAdapter(Context context,RealmResults<FavoriteListRealm> results){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        update(results);


    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView movieThumbnail;
        private TextView movieTitle;
        private TextView movieYear;

        public ViewHolder(View itemView) {
            super(itemView);


            movieThumbnail = (ImageView) itemView.findViewById(R.id.video_poster);
            movieTitle = (TextView) itemView.findViewById(R.id.video_name);
            movieYear = (TextView) itemView.findViewById(R.id.video_year);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.itemClicked(v,getAdapterPosition());

            }
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View view = layoutInflater.inflate(R.layout.item_grid_video,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavoriteListRealm favorite = mResults.get(position);
        holder.movieTitle.setText(favorite.getRealmMovieTitle());
        holder.movieYear.setText(String.valueOf(favorite.getRealmMovieYear()));
        String url = favorite.getRealmThumbnailUrl();
        Glide.with(context)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(context,10,3))
                .into(holder.movieThumbnail);

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void update(RealmResults<FavoriteListRealm> results){
        this.mResults = results;
        notifyDataSetChanged();
    }


    public interface ClickListener {
        void itemClicked(View view, int position);
    }
    public void setClickListener(ClickListener clickListener) {

        this.clickListener = clickListener;
    }
}