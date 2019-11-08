package com.sample.myapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sample.myapplication.R;
import com.sample.myapplication.databinding.ActivityMainBinding;
import com.sample.myapplication.db.FeedDatabase;
import com.sample.myapplication.model.FeedData;
import com.sample.myapplication.viewmodel.FeedViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private FeedViewModel feedViewModel;
    private FeedAdapter feedAdapter;
    private static FeedDatabase feedDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();

        feedViewModel.getFacts();

    }

    private void init() {
        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        feedViewModel.setmContext(MainActivity.this);
        feedViewModel.setFeedDatabase(getFeedDb(this));
        feedAdapter = new FeedAdapter();

        activityMainBinding.rvFeeds.setAdapter(feedAdapter);
        setupActionables();

        feedViewModel.getFeedWrapperMutableLiveData().observe(this, new Observer<List<FeedData>>() {
            @Override
            public void onChanged(List<FeedData> feedData) {
                if (!feedData.isEmpty()) {
                    if (activityMainBinding.refreshFeeds.isRefreshing()) {
                        activityMainBinding.refreshFeeds.setRefreshing(false);
                    }
                    activityMainBinding.progressBar.setVisibility(View.GONE);
                    feedAdapter.setFeedData(feedData);
                }
            }
        });
        feedViewModel.getFeedTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(s);
                }
            }
        });

    }

    private void setupActionables() {
        activityMainBinding.refreshFeeds.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //feedViewModel.getFacts(MainActivity.this);
                feedViewModel.removeAllOldFeeds();
                feedAdapter.clearAdapterData();
                activityMainBinding.progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public static FeedDatabase getFeedDb(Context context) {
        if (feedDatabase == null)
            feedDatabase = Room.databaseBuilder(context, FeedDatabase.class, "FeedDb.db").build();
        return feedDatabase;
    }
}
