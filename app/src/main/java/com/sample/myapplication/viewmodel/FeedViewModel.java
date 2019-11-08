package com.sample.myapplication.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sample.myapplication.db.FeedDatabase;
import com.sample.myapplication.model.FeedData;
import com.sample.myapplication.repository.FeedRepository;
import com.sample.myapplication.repository.OnResponse;

import java.util.List;

public class FeedViewModel extends ViewModel {
    private MutableLiveData<List<FeedData>> feedWrapperMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> feedTitle = new MutableLiveData<>();
    private MutableLiveData<Integer> cachedFeedCounts = new MutableLiveData<>();
    private FeedRepository feedRepository;
    private Context mContext;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    OnResponse onResponse = new OnResponse() {


        @Override
        public void onRowsUpdated(List<FeedData> feedData) {
            feedWrapperMutableLiveData.postValue(feedData);
        }

        @Override
        public void onTitleUpdated(String title) {
            feedTitle.postValue(title);
        }

        @Override
        public void onFailure(String msg) {
            Log.e("Network Failure", "" + msg);
        }
    };

    public void setFeedDatabase(FeedDatabase feedDatabase) {
        feedRepository = new FeedRepository(feedDatabase);
    }

    public MutableLiveData<List<FeedData>> getFeedWrapperMutableLiveData() {
        if (feedWrapperMutableLiveData == null)
            feedWrapperMutableLiveData = new MutableLiveData<>();
        return feedWrapperMutableLiveData;
    }

    public MutableLiveData<String> getFeedTitle() {
        return feedTitle;
    }

    public void getFacts() {
        feedRepository.getDatabaseRecordCount(cachedFeedCounts);

        cachedFeedCounts.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer <= 0) {
                    feedRepository.getDataFromNetwork(mContext, onResponse);
                } else {
                    feedRepository.fetchFeedsFromCache(onResponse);
                }
            }
        });

    }

    public void removeAllOldFeeds() {
        feedRepository.deleteAllFeeds(mContext, onResponse);
    }

}
