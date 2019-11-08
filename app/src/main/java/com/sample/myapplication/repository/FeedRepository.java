package com.sample.myapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sample.myapplication.appexceptions.NoConnectionException;
import com.sample.myapplication.db.FeedDatabase;
import com.sample.myapplication.model.FeedData;
import com.sample.myapplication.model.FeedWrapper;
import com.sample.myapplication.network.ApiClient;
import com.sample.myapplication.network.ApiInterface;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FeedRepository {
    private Retrofit builder;
    private ApiInterface apiInterface;
    private Disposable disposable;
    private FeedDatabase feedDatabase;

    public FeedRepository(FeedDatabase feedDatabase) {
        this.feedDatabase = feedDatabase;
    }

    public void getDataFromNetwork(Context context, final OnResponse onResponse) {
        try {
            builder = ApiClient.getClient(context);
            apiInterface = builder.create(ApiInterface.class);
            Observable<FeedWrapper> newsFeedWrapperObservable = apiInterface.getFacts();
            newsFeedWrapperObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<FeedWrapper>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(FeedWrapper newsFeedWrapper) {
                    if (newsFeedWrapper != null) {
                        insertFeeds(newsFeedWrapper.getRows(), onResponse);
                        onResponse.onTitleUpdated(newsFeedWrapper.getTitle());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    onResponse.onFailure(e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        } catch (NoConnectionException e) {
            e.printStackTrace();
            onResponse.onFailure(e.getMessage());
        }
    }

    private void insertFeeds(final List<FeedData> feedData, final OnResponse onResponse) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                List<Long> ids = feedDatabase.feedDataDao().insertData(feedData);
                Log.e("::", ":" + ids);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                fetchFeedsFromCache(onResponse);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void fetchFeedsFromCache(final OnResponse onResponse) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                onResponse.onRowsUpdated(feedDatabase.feedDataDao().getData());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });


    }

    public void getDatabaseRecordCount(final MutableLiveData<Integer> counts) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                counts.postValue(feedDatabase.feedDataDao().getRecordCounts());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void deleteAllFeeds(final Context mContext, final OnResponse onResponse) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                feedDatabase.feedDataDao().deleteAllFeeds();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                getDataFromNetwork(mContext, onResponse);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
