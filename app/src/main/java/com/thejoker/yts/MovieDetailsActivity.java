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
    private ArrayList<MovieDetails> detailsUrl = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent i = getIntent();
        String movieId = i.getExtras().getString("movieId");
        volleySingleton = VolleySingleton.getsInstance();
        mRequestQueue = VolleySingleton.getmRequestQueue();
        parseMovieDetails();



//        Bundle b = new Bundle();
//        b.putString("movie_Id", movieId);
//        MovieDetailsFragment mdFragment = new MovieDetailsFragment();
//        mdFragment.setArguments(b);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.replace(R.id.movie_detail_frame,mdFragment);
//        ft.commit();
    }
    public void parseMovieDetails(){



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(100), new Response.Listener<JSONObject>() {
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
                    int movieRating = movieDetailsObject.getInt(Keys.EndPointMovieDetails.KEYS_RATING);
                    String movieSynopsis = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_DESCRIPTION);
                    String moviePosterUrl = movieDetailsObject.getString(Keys.EndPointMovieDetails.KEYS_COVER);
                    movieDetails.setTitle(movieTitle);
                    movieDetails.setRating(movieRating);
                    movieDetails.setSummary(movieSynopsis);
                    movieDetails.setUrlThumbnail(moviePosterUrl);
                    detailsMovies.add(movieDetails);

                    JSONArray torrentDownloadLinks = movieDetailsObject.getJSONArray(Keys.EndPointMovieDetails.KEYS_TORRENTS);
                    for(int i=0;i<torrentDownloadLinks.length();i++) {
                        JSONObject urlInfo = torrentDownloadLinks.getJSONObject(i);
                        String urlTorrent = urlInfo.getString("url");
                        String quality = urlInfo.getString("quality");
                        String fileSize = urlInfo.getString("size");

                        movieDetails.setDownloadLink(urlTorrent);
                        movieDetails.setQuality(quality);
                        movieDetails.setFileSize(fileSize);
                        detailsMovies.add(movieDetails);
                    }
                    Toast.makeText(getApplicationContext(),detailsMovies.toString(),Toast.LENGTH_LONG).show();







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
}

