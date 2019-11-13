package com.sample.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.sample.myapplication.R
import com.sample.myapplication.databinding.ActivityMainBinding
import com.sample.myapplication.db.FeedDatabase
import com.sample.myapplication.model.FeedData
import com.sample.myapplication.viewmodel.FeedViewModel

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null
    private var feedViewModel: FeedViewModel? = null
    private var feedAdapter: FeedAdapter? = null

    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
        feedViewModel!!.getFacts()
    }

    private fun init() {
        feedViewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        feedViewModel!!.setmContext(this@MainActivity)
        feedViewModel!!.setFeedDatabase(getFeedDb(this))
        feedAdapter = FeedAdapter()

        activityMainBinding!!.rvFeeds.setAdapter(feedAdapter)
        setupActionables()

        feedViewModel!!.getFeedWrapperMutableLiveData().observe(this, object : Observer<List<FeedData>>() {
            @Override
            fun onChanged(feedData: List<FeedData>) {
                if (!feedData.isEmpty()) {
                    if (activityMainBinding!!.refreshFeeds.isRefreshing()) {
                        activityMainBinding!!.refreshFeeds.setRefreshing(false)
                    }
                    activityMainBinding!!.progressBar.setVisibility(View.GONE)
                    feedAdapter!!.setFeedData(feedData)
                }
            }
        })
        feedViewModel!!.getFeedTitle().observe(this, object : Observer<String>() {
            @Override
            fun onChanged(s: String) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(s)
                }
            }
        })

    }

    private fun setupActionables() {
        activityMainBinding!!.refreshFeeds.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener() {
            @Override
            fun onRefresh() {
                //feedViewModel.getFacts(MainActivity.this);
                feedViewModel!!.removeAllOldFeeds()
                feedAdapter!!.clearAdapterData()
                activityMainBinding!!.progressBar.setVisibility(View.VISIBLE)
            }
        })

    }

    @Override
    fun onBackPressed() {
        super.onBackPressed()

    }

    companion object {
        private var feedDatabase: FeedDatabase? = null

        fun getFeedDb(context: Context): FeedDatabase? {
            if (feedDatabase == null)
                feedDatabase = Room.databaseBuilder(context, FeedDatabase::class.java, "FeedDb.db").build()
            return feedDatabase
        }
    }
}
