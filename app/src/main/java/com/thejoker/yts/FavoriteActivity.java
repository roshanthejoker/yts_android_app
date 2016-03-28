package com.thejoker.yts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class FavoriteActivity extends AppCompatActivity implements FavoriteListAdapter.ClickListener {
    private Toolbar appBar;
    private Realm mRealm;
    private RecyclerView mRecyclerView;
    private FavoriteListAdapter mAdapter;
    private RealmResults<FavoriteListRealm> mResults;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        appBar = (Toolbar)findViewById(R.id.favorite_app_bar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(FavoriteListRealm.class).findAllAsync();
        mAdapter = new FavoriteListAdapter(this,mResults);
        mAdapter.setClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.favorite_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(mAdapter);


    }
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            mAdapter.update(mResults);
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(realmChangeListener);
    }

    @Override
    public void itemClicked(View view, int position) {
        mRealm.beginTransaction();
        String id = String.valueOf(mResults.get(position).getRealmMovieId());
        mRealm.commitTransaction();
        Intent i = new Intent(this,MovieDetailsActivity.class);
        i.putExtra("movieId", id);
        startActivity(i);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
