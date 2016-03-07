package com.thejoker.yts;

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
import android.widget.EditText;
import android.widget.Toast;

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

        MovieListFragment movieListFragment = new MovieListFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.movie_list_frame, movieListFragment);
        ft.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        EditText txtSearch = (EditText)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHint(R.string.search_hint);
        txtSearch.setHintTextColor(Color.GRAY);
        txtSearch.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryText));


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
                ft.addToBackStack("searchFragment");
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
}
