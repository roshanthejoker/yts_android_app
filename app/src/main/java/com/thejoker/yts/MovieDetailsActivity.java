package com.thejoker.yts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailsActivity extends AppCompatActivity {
    TextView movieIdText;
    private VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private ArrayList<MovieDetails> detailsMovies = new ArrayList<>();
    private List<String> detailsMovieGenre = new ArrayList<>();
    private ArrayList<MovieDownloadDetails>downloadDetails = new ArrayList<>();
    private String movieTitle ;
    private String movieSummary ;
    private int movieYear;
    private double movieRating;
    private String movieYoutubeId;
    private String movieUrlThumbnail;
    private String movieDownloadLink720p;
    private String movieQuality720p;
    private String moviefileSize720p;
    private String movieDownloadLink1080p;
    private String movieQuality1080p;
    private String moviefileSize1080p;
    private int  movieRunTime;
    private String movieCertificate;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private TextView movieTitleTextView;
    private TextView movieReleaseDate;
    private TextView movieGenresText;
    private TextView movieRuntimeText;
    private TextView movieCertificationText;
    private TextView movieRatingText;
    private LinearLayout layoutDetails;
    private ExpandableTextView movieSynopsisText;
    private AppBarLayout mAppBarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar_movie_details);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        movieTitleTextView = (TextView)findViewById(R.id.movie_title);
        movieReleaseDate = (TextView)findViewById(R.id.movie_release_date);
        movieGenresText = (TextView)findViewById(R.id.movie_genre);
        movieRuntimeText = (TextView)findViewById(R.id.movie_runtime);
        movieCertificationText = (TextView)findViewById(R.id.movie_certification);
        movieRatingText = (TextView)findViewById(R.id.movie_rating);
        movieSynopsisText= (ExpandableTextView)findViewById(R.id.expand_text_view);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbarlayout);





        Intent i = getIntent();
        String movieIdString = i.getExtras().getString("movieId");
        int movieId = Integer.parseInt(movieIdString);
        volleySingleton = VolleySingleton.getsInstance();
        mRequestQueue = VolleySingleton.getmRequestQueue();
        parseMovieDetails(movieId,new VolleyCallback() {
            @Override
            public void onSuccess() {
                movieYoutubeId = detailsMovies.get(0).getYoutube_id();
                movieTitle = detailsMovies.get(0).getTitle();
                movieUrlThumbnail=detailsMovies.get(0).getUrlThumbnail();
                movieSummary = detailsMovies.get(0).getSummary();
                movieYear = detailsMovies.get(0).getYear();
                movieRating = detailsMovies.get(0).getRating();
                Toast.makeText(getApplicationContext(),Double.toString(movieRating),Toast.LENGTH_LONG).show();
                double rating =  (movieRating*100.0)/10.0;

                movieRunTime = detailsMovies.get(0).getRunTime();
                String movieRunTimeString = Integer.toString(movieRunTime);
                movieCertificate=detailsMovies.get(0).getMpaaRating();


                movieQuality720p = downloadDetails.get(0).getQuality();
                movieDownloadLink720p = downloadDetails.get(0).getDownloadLink();
                moviefileSize720p = downloadDetails.get(0).getFileSize();
                movieQuality1080p = downloadDetails.get(1).getQuality();
                movieDownloadLink1080p = downloadDetails.get(1).getDownloadLink();
                moviefileSize1080p = downloadDetails.get(1).getFileSize();
                setMoviePoster(movieUrlThumbnail);
                movieTitleTextView.setText(movieTitle);
                movieGenresText.setText(detailsMovieGenre.toString().replaceAll("\\[|\\]", ""));
                movieRuntimeText.setText(movieRunTimeString+" minutes");
                if(movieCertificate=="R"){
                    layoutDetails = (LinearLayout)findViewById(R.id.details_area);
                    layoutDetails.setBackgroundColor(Color.parseColor("#e23131"));
                }
                movieCertificationText.setText(movieCertificate);
                movieRatingText.setText(rating+"%");
                movieSynopsisText.setText(movieSummary);






            }
        });


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
                    double movieRating = movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_RATING);
                    String movieSynopsis = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_DESCRIPTION);
                    String moviePosterUrl = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_COVER);
                    String movieYoutubeId = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEY_YOUTUBE_ID);
                    int  movieRunTime=movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_RUNTIME);
                    String movieCertificate=movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_CERTIFICATE);
                    movieDetails.setTitle(movieTitle);
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
                UrlEndPoints.URL_PARAM_ID+movieId;
    }
    public interface VolleyCallback {

        void onSuccess();
    }

    public void setMoviePoster(String urlThumbnail){
        ImageView posterHolder = (ImageView) findViewById(R.id.movie_fanart);
        ImageView minyPoster = (ImageView)findViewById(R.id.movie_poster);
        Glide.with(getApplicationContext())
                .load(urlThumbnail)
                .centerCrop()
                .crossFade()
                .into(posterHolder);
        Glide.with(getApplicationContext())
                .load(urlThumbnail)
                .crossFade()
                .fitCenter().centerCrop()
                .into(minyPoster);

    }


}

