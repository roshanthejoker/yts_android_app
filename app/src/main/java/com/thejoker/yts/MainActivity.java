package com.thejoker.yts;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.thejoker.yts.Keys.EndPointMovieDetails;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private Context context;
    public static final String YTS_MOVIE_LIST = "https://yts.ag/api/v2/list_movies.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = VolleySingleton.getmRequestQueue();
        sendRequest();

    }

    public void sendRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(5064),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJsonResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void parseJsonResponse(JSONObject response){
        if(response==null||response.length()==0){
            Toast.makeText(getApplicationContext(),"Null Object",Toast.LENGTH_LONG).show();
            return;
        }
        try {
            JSONObject movieDetails = response.getJSONObject("data").getJSONObject(EndPointMovieDetails.KEYS_MOVIE);
            String title = movieDetails.optString(EndPointMovieDetails.KEYS_TITLE_LONG);
            Toast.makeText(getApplicationContext(),title,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static String getUrl(int id)
    {
        return UrlEndPoints.URL_MOVIE_DETAILS+
                UrlEndPoints.URl_CHAR_QUESTION+
                UrlEndPoints.URL_PARAM_ID+id;
    }

}
