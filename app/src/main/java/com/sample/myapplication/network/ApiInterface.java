package com.sample.myapplication.network;


import com.sample.myapplication.model.FeedWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("facts")
    Observable<FeedWrapper> getFacts();
}
