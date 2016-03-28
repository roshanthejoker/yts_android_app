package com.thejoker.yts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;

public class MainActivity extends AppCompatActivity {
    private Toolbar appBar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private SearchView mSearchView;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private AppCompatSpinner mSpinner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBar = (Toolbar) findViewById(R.id.app_bar);
        appBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addGenres();






        MovieListFragment movieListFragment = new MovieListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.movie_list_frame, movieListFragment);
        ft.commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorites:
                Intent i = new Intent(this,FavoriteActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        EditText txtSearch = (EditText)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHint(R.string.search_hint);
        txtSearch.setHintTextColor(Color.GRAY);
        txtSearch.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryText));



        mSpinner.setSelection(0, true);
        View v = mSpinner.getSelectedView();
        ((TextView)v).setTextColor(Color.WHITE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("searchQuery", query);
                MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
                movieSearchFragment.setArguments(bundle);
                ft.replace(R.id.movie_list_frame, movieSearchFragment);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItem menuItem =menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.movie_list_frame, new MovieListFragment());
                fm.popBackStack();
                ft.commit();
                return true;
            }
        });


        return true;
    }



    @Override
    public void onBackPressed() {
        mBackPressed = System.currentTimeMillis();
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }


    }
    public void addGenres(){
        mSpinner = (AppCompatSpinner)findViewById(R.id.movie_sort_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Spinner_Content,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position).toString();
                ((TextView) view).setTextColor(Color.WHITE);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("genreQuery", item);
                MovieListFragment movieListFragment = new MovieListFragment();
                movieListFragment.setArguments(bundle);
                ft.replace(R.id.movie_list_frame, movieListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
