package com.sample.myapplication.repository

import android.content.Context
import android.util.Log

import androidx.lifecycle.MutableLiveData

import com.sample.myapplication.appexceptions.NoConnectionException
import com.sample.myapplication.db.FeedDatabase
import com.sample.myapplication.model.FeedData
import com.sample.myapplication.model.FeedWrapper
import com.sample.myapplication.network.ApiClient
import com.sample.myapplication.network.ApiInterface

import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class FeedRepository(private val feedDatabase: FeedDatabase) {
    private var builder: Retrofit? = null
    private var apiInterface: ApiInterface? = null
    private var disposable: Disposable? = null

    fun getDataFromNetwork(context: Context, onResponse: OnResponse) {
        try {
            builder = ApiClient.getClient(context)
            apiInterface = builder!!.create(ApiInterface::class.java)
            val newsFeedWrapperObservable = apiInterface!!.facts()
            newsFeedWrapperObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<FeedWrapper> {

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }


                        override fun onNext(newsFeedWrapper: FeedWrapper) {
                            if (newsFeedWrapper != null) {
                                insertFeeds(newsFeedWrapper!!.rows!!, onResponse)
                                onResponse.onTitleUpdated(newsFeedWrapper!!.title!!)
                            }
                        }


                        override fun onError(e: Throwable) {
                            onResponse.onFailure(e.message!!)
                        }

                        override fun onComplete() {

                        }
                    })
        } catch (e: NoConnectionException) {
            e.printStackTrace()
            onResponse.onFailure(e.message!!)
        }

    }

    private fun insertFeeds(feedData: List<FeedData>, onResponse: OnResponse) {
        Completable.fromAction(object : Action {

            @Throws(Exception::class)
            override fun run() {
                val ids = feedDatabase.feedDataDao().insertData(feedData)
                Log.e("::", ":$ids")
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver {
            override
            fun onSubscribe(d: Disposable) {

            }

            override
            fun onComplete() {
                fetchFeedsFromCache(onResponse)
            }

            override
            fun onError(e: Throwable) {

            }
        })
    }

    fun fetchFeedsFromCache(onResponse: OnResponse) {
        Completable.fromAction(object : Action {
            override
            @Throws(Exception::class)
            fun run() {
                onResponse.onRowsUpdated(feedDatabase.feedDataDao().data)
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver {
            override
            fun onSubscribe(d: Disposable) {

            }

            override
            fun onComplete() {

            }

            override
            fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })


    }

    fun getDatabaseRecordCount(counts: MutableLiveData<Integer>) {
        Completable.fromAction(object : Action {
            override
            @Throws(Exception::class)
            fun run() {
                counts.postValue(feedDatabase.feedDataDao().recordCounts!!)
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver {
            override
            fun onSubscribe(d: Disposable) {

            }

            override
            fun onComplete() {

            }

            override
            fun onError(e: Throwable) {

            }
        })
    }

    fun deleteAllFeeds(mContext: Context, onResponse: OnResponse) {
        Completable.fromAction(object : Action {
            override
            @Throws(Exception::class)
            fun run() {
                feedDatabase.feedDataDao().deleteAllFeeds()
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver {
            override
            fun onSubscribe(d: Disposable) {

            }

            override
            fun onComplete() {
                getDataFromNetwork(mContext, onResponse)
            }

            override
            fun onError(e: Throwable) {

            }
        })
    }
}
