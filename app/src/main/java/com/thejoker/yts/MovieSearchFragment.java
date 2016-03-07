package com.thejoker.yts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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


public class MovieSearchFragment extends Fragment implements  ListMoviesAdapter.Clicklistener,SwipeRefreshLayout.OnRefreshListener{
    private RequestQueue mRequestQueue;
    private VolleySingleton volleySingleton;
    private View mView;
    public ArrayList<MovieList> listSearchMovies = new ArrayList<>();
    private RecyclerView searchMoviesRecyclerView;
    private  ListMoviesAdapter searchMoviesAdapter;
    private int pageNumber = 1;
    private ProgressBar Spinner;
    private String searchQuery;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        this.mView = view;

        Bundle extras = getArguments();
        searchQuery = extras.getString("searchQuery");

        searchMoviesRecyclerView = (RecyclerView)  view.findViewById(R.id.list_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        searchMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        searchMoviesAdapter = new ListMoviesAdapter(getActivity());
        searchMoviesAdapter.setclickListener(this);
        searchMoviesRecyclerView.setAdapter(searchMoviesAdapter);

        searchMoviesRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                int lastFirstViewPosition = ((GridLayoutManager) searchMoviesRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                (searchMoviesRecyclerView.getLayoutManager()).scrollToPosition(lastFirstViewPosition);
                updateNextMovieList(searchQuery);
            }
        });
        updateMovieList(searchQuery);
        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.colorPrimary);
        if(swipeLayout.isRefreshing()){
            Spinner.setVisibility(View.GONE);
        }

        return view;


    }
    public void updateMovieList(String SearchQuery) {
        volleySingleton = VolleySingleton.getsInstance();
        mRequestQueue = VolleySingleton.getmRequestQueue();
        Spinner = (ProgressBar) mView.findViewById(R.id.progress_spin);
        Spinner.setIndeterminate(true);
        Spinner.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(),getUrl(1,SearchQuery),Toast.LENGTH_LONG).show();




        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(pageNumber,SearchQuery),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response == null || response.length() == 0) {
                                Toast.makeText(getActivity(), "Null Object", Toast.LENGTH_LONG).show();
                            }
                            if(response.getJSONObject("data").getInt("movie_count")==0){
                                Snackbar.make(getView(), "No Movies Found", Snackbar.LENGTH_LONG).show();
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
                                listSearchMovies.add(movieList);
                            }

                        }
                        catch(JSONException exception){

                            exception.printStackTrace();
                        }
                        searchMoviesAdapter.setListMovies(listSearchMovies);
                        if(swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        Spinner.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeLayout.setRefreshing(false);

                Spinner.setVisibility(View.GONE);

                Snackbar.make(getView(), "Please Check Your Internet Connection!", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        mRequestQueue.add(jsonObjectRequest);


    }
    public void updateNextMovieList(String searchQuery) {
        volleySingleton = VolleySingleton.getsInstance();
        mRequestQueue = VolleySingleton.getmRequestQueue();
        pageNumber = pageNumber+1;


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(pageNumber, searchQuery),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response == null || response.length() == 0) {
                                Toast.makeText(getActivity(), "Null Object", Toast.LENGTH_LONG).show();
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
                                listSearchMovies.add(movieList);
                            }

                            searchMoviesAdapter.setListMovies(listSearchMovies);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        searchMoviesAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar.make(mView, "Please Check Your Internet Connection!", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        mRequestQueue.add(jsonObjectRequest);

    }


    public static String getUrl(int pageNo,String searchQuery)
    {
        return UrlEndPoints.URL_MOVIE_LIST+
                UrlEndPoints.URl_CHAR_QUESTION+
                UrlEndPoints.URL_PARAM_PAGE+pageNo+
                UrlEndPoints.URl_CHAR_AMPERSAND+
                UrlEndPoints.URL_PARAM_QUERY+searchQuery;
    }


    @Override
    public void itemClicked(View view, int position) {
        String id = String.valueOf(listSearchMovies.get(position).getId());
        Intent i = new Intent(getActivity(),MovieDetailsActivity.class);
        i.putExtra("movieId", id);

        startActivity(i);
    }

    @Override
    public void onRefresh() {
        updateMovieList(searchQuery);
    }
}
