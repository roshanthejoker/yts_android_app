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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ListMoviesAdapter extends RecyclerView.Adapter<ListMoviesAdapter.ViewHolderListMovies> {
    private ArrayList<MovieList> listMovies = new ArrayList<>();
    private VolleySingleton volleySingleton;
    private  Context context;
    private LayoutInflater layoutInflater;
    private  Clicklistener clickListener;
    public ListMoviesAdapter(Context context){
        layoutInflater=LayoutInflater.from(context);
        volleySingleton=VolleySingleton.getsInstance();
       this.context=context;

    }
    public void setListMovies(ArrayList<MovieList> listMovies){
        this.listMovies = listMovies;
        notifyItemRangeChanged(0, listMovies.size());

    }







   public   class ViewHolderListMovies extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView movieThumbnail;
        private TextView movieTitle;
        private TextView movieYear;

        public ViewHolderListMovies(View itemView) {
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
    public ViewHolderListMovies onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_grid_video,parent,false);
        ViewHolderListMovies viewHolder = new ViewHolderListMovies(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderListMovies holder, int position)  {
            MovieList currentMovie = listMovies.get(position);
        holder.itemView.setTag(listMovies.get(position));
        holder.movieTitle.setText(currentMovie.getTitle());
        holder.movieYear.setText(Integer.toString(currentMovie.getYear()));
        String url = currentMovie.getUrlThumbnail();
        Glide.with(context)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(context,10,3))
                .into(holder.movieThumbnail);


    }




    @Override
    public int getItemCount() {

        return listMovies.size();
    }

public void setclickListener(Clicklistener clickListener){

    this.clickListener = clickListener;
}
    public interface Clicklistener{
         void itemClicked(View view,int position);
    }
}
