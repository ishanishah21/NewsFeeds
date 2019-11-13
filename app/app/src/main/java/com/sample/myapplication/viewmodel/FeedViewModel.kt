package com.sample.myapplication.viewmodel

import android.content.Context
import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

import com.sample.myapplication.db.FeedDatabase
import com.sample.myapplication.model.FeedData
import com.sample.myapplication.repository.FeedRepository
import com.sample.myapplication.repository.OnResponse

class FeedViewModel : ViewModel() {
    private var feedWrapperMutableLiveData: MutableLiveData<List<FeedData>>? = MutableLiveData()
    val feedTitle: MutableLiveData<String> = MutableLiveData()
    private val cachedFeedCounts = MutableLiveData()
    private var feedRepository: FeedRepository? = null
    private var mContext: Context? = null

    internal var onResponse: OnResponse = object : OnResponse() {


        @Override
        fun onRowsUpdated(feedData: List<FeedData>) {
            feedWrapperMutableLiveData!!.postValue(feedData)
        }

        @Override
        fun onTitleUpdated(title: String) {
            feedTitle.postValue(title)
        }

        @Override
        fun onFailure(msg: String) {
            Log.e("Network Failure", "" + msg)
        }
    }

    fun setmContext(mContext: Context) {
        this.mContext = mContext
    }

    fun setFeedDatabase(feedDatabase: FeedDatabase) {
        feedRepository = FeedRepository(feedDatabase)
    }

    fun getFeedWrapperMutableLiveData(): MutableLiveData<List<FeedData>> {
        if (feedWrapperMutableLiveData == null)
            feedWrapperMutableLiveData = MutableLiveData()
        return feedWrapperMutableLiveData
    }

    fun getFacts() {
        feedRepository!!.getDatabaseRecordCount(cachedFeedCounts)

        cachedFeedCounts.observeForever(object : Observer<Integer>() {
            @Override
            fun onChanged(integer: Integer) {
                if (integer <= 0) {
                    feedRepository!!.getDataFromNetwork(mContext, onResponse)
                } else {
                    feedRepository!!.fetchFeedsFromCache(onResponse)
                }
            }
        })

    }

    fun removeAllOldFeeds() {
        feedRepository!!.deleteAllFeeds(mContext, onResponse)
    }

}
