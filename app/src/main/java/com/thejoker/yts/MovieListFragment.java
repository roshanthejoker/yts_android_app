package com.thejoker.yts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MovieListFragment extends Fragment implements  ListMoviesAdapter.Clicklistener  {
    private RequestQueue requestQueue;
    private VolleySingleton volleySingleton;
    private Context context;
    public ArrayList<MovieList> listMovies = new ArrayList<>();
    private RecyclerView listMoviesRecyclerView;
    private  ListMoviesAdapter listMoviesAdapter;
    private int pageNumber = 1;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);

        listMoviesRecyclerView = (RecyclerView)  view.findViewById(R.id.list_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        listMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        listMoviesAdapter = new ListMoviesAdapter(getActivity());
        listMoviesAdapter.setclickListener(this);
        listMoviesRecyclerView.setAdapter(listMoviesAdapter);

        listMoviesRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                int lastFirstViewPosition = ((GridLayoutManager) listMoviesRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                (listMoviesRecyclerView.getLayoutManager()).scrollToPosition(lastFirstViewPosition);
                updateNextMovieList();
            }
        });




        updateMovieList();
        return view;


    }
    public void updateMovieList() {
        RequestQueue mrequestQueue = Volley.newRequestQueue(getActivity());



        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(pageNumber),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response == null || response.length() == 0) {
                                Toast.makeText(context, "Null Object", Toast.LENGTH_LONG).show();
                            }

                            JSONArray moviesListArray = response.getJSONObject("data").getJSONArray(Keys.EndPointMovieList.KEYS_MOVIES);
                            for (int i = 0; i < moviesListArray.length(); i++) {
                                JSONObject currentMovie = moviesListArray.getJSONObject(i);
                                int currentMovieId = currentMovie.getInt(Keys.EndPointMovieList.KEYS_ID);
                                String currentMovieTitle = currentMovie.getString(Keys.EndPointMovieList.KEYS_TITLE);
                                String currentMoviePoster = currentMovie.getString(Keys.EndPointMovieList.KEYS_THUMBNAIL);
                                long currentMovieRating = currentMovie.getLong(Keys.EndPointMovieList.KEYS_RATING);
                                int currentMovieYear = currentMovie.getInt(Keys.EndPointMovieList.KEYS_YEAR);

                                MovieList movieList = new MovieList();
                                movieList.setId(currentMovieId);
                                movieList.setTitle(currentMovieTitle);
                                movieList.setRating(currentMovieRating);
                                movieList.setYear(currentMovieYear);
                                movieList.setUrlThumbnail(currentMoviePoster);
                                listMovies.add(movieList);
                            }

                        }
                        catch(JSONException exception){
                            exception.printStackTrace();
                        }
                        listMoviesAdapter.setListMovies(listMovies);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(getView(),"Please Check Your Internet Connection!",Snackbar.LENGTH_INDEFINITE).setAction("Go to Settings",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                            }
                        }).show();
            }
        });
        mrequestQueue.add(jsonObjectRequest);


    }
    public void updateNextMovieList() {
        RequestQueue mrequestQueue = Volley.newRequestQueue(getActivity());
                pageNumber = pageNumber+1;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(pageNumber),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                                if (response == null || response.length() == 0) {
                                    Toast.makeText(context, "Null Object", Toast.LENGTH_LONG).show();
                                }

                                JSONArray moviesListArray = response.getJSONObject("data").getJSONArray(Keys.EndPointMovieList.KEYS_MOVIES);
                                for (int i = 0; i < moviesListArray.length(); i++) {
                                    JSONObject currentMovie = moviesListArray.getJSONObject(i);
                                    int currentMovieId = currentMovie.getInt(Keys.EndPointMovieList.KEYS_ID);
                                    String currentMovieTitle = currentMovie.getString(Keys.EndPointMovieList.KEYS_TITLE);
                                    String currentMoviePoster = currentMovie.getString(Keys.EndPointMovieList.KEYS_THUMBNAIL);
                                    int currentMovieYear = currentMovie.getInt(Keys.EndPointMovieList.KEYS_YEAR);
                                    long currentMovieRating = currentMovie.getLong(Keys.EndPointMovieList.KEYS_RATING);

                                    MovieList movieList = new MovieList();
                                    movieList.setId(currentMovieId);
                                    movieList.setTitle(currentMovieTitle);
                                    movieList.setRating(currentMovieRating);
                                    movieList.setYear(currentMovieYear);
                                    movieList.setUrlThumbnail(currentMoviePoster);
                                    listMovies.add(movieList);
                                }

                            listMoviesAdapter.setListMovies(listMovies);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listMoviesAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getView(),"Please Check Your Internet Connection!",Snackbar.LENGTH_INDEFINITE).setAction("Go to Settings",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0);
                            }
                        }).show();

            }
        });
        mrequestQueue.add(jsonObjectRequest);

    }

    public static String getUrl(int pageNo)
    {
        return UrlEndPoints.URL_MOVIE_LIST+
        UrlEndPoints.URl_CHAR_QUESTION+
        UrlEndPoints.URL_PARAM_PAGE+pageNo;
    }


    @Override
    public void itemClicked(View view, int position) {
        String id = String.valueOf(listMovies.get(position).getId());
        Intent i = new Intent(getActivity(),MovieDetailsActivity.class);
        i.putExtra("movieId", id);

        startActivity(i);
    }
}
