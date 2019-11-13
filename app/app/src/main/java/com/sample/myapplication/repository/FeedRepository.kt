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
            val newsFeedWrapperObservable = apiInterface!!.getFacts()
            newsFeedWrapperObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<FeedWrapper>() {
                        @Override
                        fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        @Override
                        fun onNext(newsFeedWrapper: FeedWrapper?) {
                            if (newsFeedWrapper != null) {
                                insertFeeds(newsFeedWrapper!!.getRows(), onResponse)
                                onResponse.onTitleUpdated(newsFeedWrapper!!.getTitle())
                            }
                        }

                        @Override
                        fun onError(e: Throwable) {
                            onResponse.onFailure(e.getMessage())
                        }

                        @Override
                        fun onComplete() {

                        }
                    })
        } catch (e: NoConnectionException) {
            e.printStackTrace()
            onResponse.onFailure(e.getMessage())
        }

    }

    private fun insertFeeds(feedData: List<FeedData>, onResponse: OnResponse) {
        Completable.fromAction(object : Action() {
            @Override
            @Throws(Exception::class)
            fun run() {
                val ids = feedDatabase.feedDataDao().insertData(feedData)
                Log.e("::", ":$ids")
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver() {
            @Override
            fun onSubscribe(d: Disposable) {

            }

            @Override
            fun onComplete() {
                fetchFeedsFromCache(onResponse)
            }

            @Override
            fun onError(e: Throwable) {

            }
        })
    }

    fun fetchFeedsFromCache(onResponse: OnResponse) {
        Completable.fromAction(object : Action() {
            @Override
            @Throws(Exception::class)
            fun run() {
                onResponse.onRowsUpdated(feedDatabase.feedDataDao().getData())
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver() {
            @Override
            fun onSubscribe(d: Disposable) {

            }

            @Override
            fun onComplete() {

            }

            @Override
            fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })


    }

    fun getDatabaseRecordCount(counts: MutableLiveData<Integer>) {
        Completable.fromAction(object : Action() {
            @Override
            @Throws(Exception::class)
            fun run() {
                counts.postValue(feedDatabase.feedDataDao().getRecordCounts())
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver() {
            @Override
            fun onSubscribe(d: Disposable) {

            }

            @Override
            fun onComplete() {

            }

            @Override
            fun onError(e: Throwable) {

            }
        })
    }

    fun deleteAllFeeds(mContext: Context, onResponse: OnResponse) {
        Completable.fromAction(object : Action() {
            @Override
            @Throws(Exception::class)
            fun run() {
                feedDatabase.feedDataDao().deleteAllFeeds()
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(object : CompletableObserver() {
            @Override
            fun onSubscribe(d: Disposable) {

            }

            @Override
            fun onComplete() {
                getDataFromNetwork(mContext, onResponse)
            }

            @Override
            fun onError(e: Throwable) {

            }
        })
    }
}
