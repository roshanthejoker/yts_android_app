package com.thejoker.yts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MovieDetailsActivity extends AppCompatActivity {
    TextView movieIdText;
    private VolleySingleton volleySingleton;
    private RequestQueue mRequestQueue;
    private ArrayList<MovieDetails> detailsMovies = new ArrayList<>();
    private ArrayList<MovieDownloadDetails>downloadDetails = new ArrayList<>();
    private String movieTitle ;
    private String movieSummary ;
    private int movieYear;
    private long movieRating;
    private String movieYoutubeId;
    private String movieUrlThumbnail;
    private String movieDownloadLink720p;
    private String movieQuality720p;
    private String moviefileSize720p;
    private String movieDownloadLink1080p;
    private String movieQuality1080p;
    private String moviefileSize1080p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
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
                movieQuality720p = downloadDetails.get(0).getQuality();
                movieDownloadLink720p = downloadDetails.get(0).getDownloadLink();
                moviefileSize720p = downloadDetails.get(0).getFileSize();
                movieQuality1080p = downloadDetails.get(1).getQuality();
                movieDownloadLink1080p = downloadDetails.get(1).getDownloadLink();
                moviefileSize1080p = downloadDetails.get(1).getFileSize();
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
                    long movieRating = movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_RATING);
                    String movieSynopsis = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_DESCRIPTION);
                    String moviePosterUrl = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_COVER);
                    String movieYoutubeId = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEY_YOUTUBE_ID);
                    movieDetails.setTitle(movieTitle);
                    movieDetails.setRating(movieRating);
                    movieDetails.setSummary(movieSynopsis);
                    movieDetails.setUrlThumbnail(moviePosterUrl);
                    movieDetails.setYoutube_id(movieYoutubeId);
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
}

