package com.thejoker.yts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.Uri;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;


public class MovieDetailsActivity extends AppCompatActivity implements MovieCastAdapter.ClickListener {
    private VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private ArrayList<MovieDetails> detailsMovies = new ArrayList<>();
    private List<String> detailsMovieGenre = new ArrayList<>();
    private ArrayList<MovieDownloadDetails>downloadDetails = new ArrayList<>();
    private ArrayList<MovieCastDetails>movieCast = new ArrayList<>();
    private String movieTitle ;
    private String movieSummary ;
    private int movieYear;
    private double movieRating;
    private String movieYoutubeId;
    private String movieUrlThumbnail;
    private int  movieRunTime;
    private String movieCertificate;
    private TextView movieTitleTextView;
    private TextView movieReleaseDate;
    private TextView movieGenresText;
    private TextView movieRuntimeText;
    private TextView movieCertificationText;
    private TextView movieRatingText;
    private LinearLayout layoutDetails;
    private ExpandableTextView movieSynopsisText;
    private Toolbar movieDetailsToolbar;
    private RecyclerView movieCastRecyclerview;
    private MovieCastAdapter movieCastAdapter;
    private String imdbId;
    private ProgressBar mProgressBar;
    private LinearLayout mContentLayout;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    public Realm mRealm;
    private boolean fav;
    private FavoriteListAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        mRealm = Realm.getDefaultInstance();
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_content);
        mContentLayout = (LinearLayout)findViewById(R.id.root_view_detail_activity);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbarlayout);
        mAppBarLayout.setExpanded(false);




        mProgressBar = (ProgressBar)findViewById(R.id.progress_spin2);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

        movieCastRecyclerview = (RecyclerView)findViewById(R.id.cast_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        movieCastRecyclerview.setLayoutManager(linearLayoutManager);
        movieCastAdapter = new MovieCastAdapter(getContext());
        movieCastAdapter.setClickListener(this);
        movieCastRecyclerview.setAdapter(movieCastAdapter);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }


        Intent i = getIntent();
        String movieIdString = i.getExtras().getString("movieId");
        final int movieId = Integer.parseInt(movieIdString);
        volleySingleton = VolleySingleton.getsInstance();
        mRequestQueue = VolleySingleton.getmRequestQueue();

        parseMovieDetails(movieId,new VolleyCallback() {
            @Override
            public void onSuccess() {


                movieTitleTextView = (TextView)findViewById(R.id.movie_title);
                movieGenresText = (TextView)findViewById(R.id.movie_genre);
                movieRuntimeText = (TextView)findViewById(R.id.movie_runtime);
                movieCertificationText = (TextView)findViewById(R.id.movie_certification);
                movieRatingText = (TextView)findViewById(R.id.movie_rating);
                movieSynopsisText= (ExpandableTextView)findViewById(R.id.expand_text_view);
                movieDetailsToolbar = (Toolbar)findViewById(R.id.toolbar_movie_details);

                Drawable backDrawable = MaterialDrawableBuilder.with(getContext())
                        .setIcon(MaterialDrawableBuilder.IconValue.ARROW_LEFT)
                        .setColor(Color.WHITE)
                        .setToActionbarSize()
                        .build();
                setSupportActionBar(movieDetailsToolbar);
                movieDetailsToolbar.setNavigationIcon(backDrawable);
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                if(checkIfExists(movieId))
                {
                    fav=false;
                }
                else{
                    fav=true;
                }
                final MaterialFavoriteButton mFavoriteButton = new MaterialFavoriteButton.Builder(MovieDetailsActivity.this)
                        .favorite(fav)
                        .color(MaterialFavoriteButton.STYLE_WHITE)
                        .type(MaterialFavoriteButton.STYLE_HEART)
                        .rotationDuration(300)
                        .create();
                movieDetailsToolbar.addView(mFavoriteButton,0);



                movieDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                movieYoutubeId = detailsMovies.get(0).getYoutube_id();
                movieTitle = detailsMovies.get(0).getTitle();
                movieUrlThumbnail = detailsMovies.get(0).getUrlThumbnail();
                movieSummary = detailsMovies.get(0).getSummary();
                movieYear = detailsMovies.get(0).getYear();
                movieRating = detailsMovies.get(0).getRating();
                double rating = (movieRating * 100.0) / 10.0;

                movieRunTime = detailsMovies.get(0).getRunTime();
                String movieRunTimeString = Integer.toString(movieRunTime);
                movieCertificate = detailsMovies.get(0).getMpaaRating();
                mAppBarLayout.setExpanded(true);


                setMoviePoster(movieUrlThumbnail);
                movieTitleTextView.setText(movieTitle);
                movieGenresText.setText(detailsMovieGenre.toString().replaceAll("\\[|\\]", ""));
                movieRuntimeText.setText(movieRunTimeString + " minutes");
                if (movieCertificate.equals("R")) {
                    layoutDetails = (LinearLayout) findViewById(R.id.details_area);
                    layoutDetails.setBackgroundColor(Color.parseColor("#e23131"));
                }
                movieCertificationText.setText(movieCertificate);
                movieRatingText.setText(rating + "%");
                movieSynopsisText.setText(movieSummary);
                mContentLayout.setVisibility(View.VISIBLE);

                mProgressBar.setVisibility(View.GONE);


                mFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if(!favorite) {
                           RealmQuery<FavoriteListRealm> query = mRealm.where(FavoriteListRealm.class);
                                    query.equalTo("realmMovieId",movieId);
                            RealmResults<FavoriteListRealm> result = query.findAll();
                            mRealm.beginTransaction();
                            result.removeLast();
                            mRealm.commitTransaction();







                        }
                        if(favorite){

                            boolean b = checkIfExists(movieId);
                            if(b)
                            {
                                mRealm.beginTransaction();
                                FavoriteListRealm favorites = mRealm.createObject(FavoriteListRealm.class);
                                favorites.setRealmMovieId(movieId);
                                favorites.setRealmMovieTitle(movieTitle);
                                favorites.setRealmThumbnailUrl(movieUrlThumbnail);
                                favorites.setRealmMovieYear(movieYear);
                                mRealm.copyToRealmOrUpdate(favorites);
                                mRealm.commitTransaction();


                            }






                        }

                    }
                });

            }

        });
    }

    public boolean checkIfExists(int id){
        RealmQuery<FavoriteListRealm> query = mRealm.where(FavoriteListRealm.class);
          RealmQuery<FavoriteListRealm> realmQuery = query.equalTo("realmMovieId",id);
        RealmResults<FavoriteListRealm> results = realmQuery.findAll();

        if(results.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }
    public Context getContext()
    {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_details,menu);
        return true;

    }



    public String getImdbUrl(String imdbId){
        return "http://m.imdb.com/name/nm"+imdbId+"/";
    }
    public void launchWebview(String imdbId){
        String packageName = "com.android.chrome";
        String url = getImdbUrl(imdbId);
        CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder();
        customTabsIntent.setToolbarColor(Color.parseColor("#14e715"));
        customTabsIntent.setShowTitle(true);
        if(chromeInstalled()){
            customTabsIntent.build().intent.setPackage("com.android.com");
        }
        customTabsIntent.build().launchUrl(MovieDetailsActivity.this, Uri.parse(url));
    }

    public boolean chromeInstalled(){
        try{
            getPackageManager().getPackageInfo("co.android.chrome",0);
            return true;

        }
        catch(Exception e){
            return false;

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.movie_download_links:
                showTorrentDownloadLinks();
                return true;
            case R.id.movie_trailer:
                showYoutubeTrailer();
                return true;
        }
       return true;
    }

    public void showYoutubeTrailer(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movieYoutubeId));
        intent.putExtra("force_fullscreen",true);
        startActivity(intent);
    }



    private void showTorrentDownloadLinks() {
        String[] a = new String[downloadDetails.size()];
        for(int i=0;i<downloadDetails.size();i++){
            a[i]=downloadDetails.get(i).getQuality();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.dailog_torrent).setItems(a, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = downloadDetails.get(which).getDownloadLink();
                Intent torrentIntent = new Intent(Intent.ACTION_VIEW);
                torrentIntent.addCategory(Intent.CATEGORY_DEFAULT);
                torrentIntent.setType("application/x-bittorent");
                torrentIntent.setData(Uri.parse(url));
                startActivity(torrentIntent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void  parseMovieDetails(int id,final VolleyCallback callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(id),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MovieDetails movieDetails = new MovieDetails();
                if (response == null || response.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Null Object", Toast.LENGTH_LONG).show();
                }

                try {

                    JSONObject movieData = response.getJSONObject("data");
                    JSONObject movieDetailsObject = movieData.getJSONObject(Keys.EndPointMovieDetails.KEYS_MOVIE);
                    String movieTitle = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_TITLE_LONG);
                    int movieYear = movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_YEAR);
                    double movieRating = movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_RATING);
                    String movieSynopsis = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_DESCRIPTION);
                    String moviePosterUrl = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_COVER);
                    String movieYoutubeId = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEY_YOUTUBE_ID);
                    int  movieRunTime=movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_RUNTIME);
                    String movieCertificate=movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_CERTIFICATE);
                    movieDetails.setTitle(movieTitle);
                    movieDetails.setYear(movieYear);
                    movieDetails.setRating(movieRating);
                    movieDetails.setSummary(movieSynopsis);
                    movieDetails.setUrlThumbnail(moviePosterUrl);
                    movieDetails.setYoutube_id(movieYoutubeId);
                    movieDetails.setRunTime(movieRunTime);
                    movieDetails.setMpaaRating(movieCertificate);
                    JSONArray movieGenre = movieDetailsObject.getJSONArray(Keys.EndPointMovieDetails.KEYS_GENRE);

                    for(int j=0;j<movieGenre.length();j++){
                        String movieGenres;
                        movieGenres=movieGenre.optString(j);
                        detailsMovieGenre.add(movieGenres);
                    }
                    detailsMovies.add(movieDetails);

                    JSONArray movieCastArray = movieDetailsObject.getJSONArray(Keys.EndPointMovieDetails.KEYS_CAST);
                    for(int j=0;j<movieCastArray.length();j++){
                        MovieCastDetails movieCastDetails = new MovieCastDetails();
                        JSONObject castInfo = movieCastArray.getJSONObject(j);
                        String actorName = castInfo.getString("name");
                        String characterName = castInfo.getString("character_name");
                        String urlImage = castInfo.optString("url_small_image");
                        String imdbId = castInfo.getString("imdb_code");
                        movieCastDetails.setName(actorName);
                        movieCastDetails.setCharacterName(characterName);
                        movieCastDetails.setImageUrl(urlImage);
                        movieCastDetails.setImdbId(imdbId);
                        movieCast.add(movieCastDetails);
                    }
                    movieCastAdapter.setCastMovies(movieCast);
                    movieCastAdapter.notifyDataSetChanged();



                    JSONArray torrentDownloadLinks = movieDetailsObject.getJSONArray(Keys.EndPointMovieDetails.KEYS_TORRENTS);
                    for(int i=0;i<torrentDownloadLinks.length();i++) {
                        MovieDownloadDetails movieDownloadDetails = new MovieDownloadDetails();
                        JSONObject urlInfo = torrentDownloadLinks.getJSONObject(i);
                        String urlTorrent = urlInfo.getString("url");
                        String quality = urlInfo.getString("quality");
                        String fileSize = urlInfo.getString("size");

                        movieDownloadDetails.setDownloadLink(urlTorrent);
                        movieDownloadDetails.setQuality(quality);
                        movieDownloadDetails.setFileSize(fileSize);
                        downloadDetails.add(movieDownloadDetails);
                    }


                    callback.onSuccess();





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jsonObjectRequest);



    }

    public String getUrl(int movieId){
        return UrlEndPoints.URL_MOVIE_DETAILS+
                UrlEndPoints.URl_CHAR_QUESTION+
                UrlEndPoints.URL_PARAM_ID+movieId+
                UrlEndPoints.URl_CHAR_AMPERSAND+
                UrlEndPoints.URL_PARAAM_ENABLE_CAST+
                UrlEndPoints.URl_CHAR_AMPERSAND+
                UrlEndPoints.URL_PARAM_SCREENSHOTS;
    }

    @Override
    public void itemClicked(View view, int position) {

        imdbId = movieCast.get(position).getImdbId();
        launchWebview(imdbId);
    }

    public interface VolleyCallback {

        void onSuccess();
    }

    public void setMoviePoster(String urlThumbnail){
        ImageView moviePoster = (ImageView)findViewById(R.id.movie_fanart);
        ImageView minyPoster = (ImageView)findViewById(R.id.movie_poster);
        Glide.with(getApplicationContext())
                .load(urlThumbnail)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(moviePoster);
        Glide.with(getApplicationContext())
                .load(urlThumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .fitCenter().centerCrop()
                .into(minyPoster);







                }



}

