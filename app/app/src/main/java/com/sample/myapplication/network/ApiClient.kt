package com.sample.myapplication.network


import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sample.myapplication.BuildConfig
import com.sample.myapplication.R
import com.sample.myapplication.appexceptions.NoConnectionException
import com.sample.myapplication.utils.NetworkUtil

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


    private var retrofit: Retrofit? = null
    private var mContext: Context? = null
    private var okHttpClient: OkHttpClient? = null

    @Throws(NoConnectionException::class)
    fun getClient(con: Context): Retrofit? {
        mContext = con
        // if (BuildConfig.DEBUG) {
        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BaseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build()

        if (NetworkUtil.getConnectionStatus(mContext) === NetworkUtil.NOT_CONNECTED) {
            throw NoConnectionException(con.getResources().getString(R.string.no_iternet))
        }

        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(logging)
            }
            builder.readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
            okHttpClient = builder.build()
        }

        return okHttpClient
    }

    @Throws(NoConnectionException::class)
    fun getClient(con: Context, baseUrl: String): Retrofit? {
        mContext = con
        //if (BuildConfig.DEBUG) {
        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getOkHttpClient())
                .build()

        if (NetworkUtil.getConnectionStatus(mContext) === NetworkUtil.NOT_CONNECTED) {
            throw NoConnectionException(con.getResources().getString(R.string.no_iternet))
        }
        return retrofit
    }

    @Throws(NoConnectionException::class)
    fun getApiClientForBackground(con: Context): ApiInterface {
        mContext = con
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)


        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

        val retrofit: Retrofit
        val builder = Retrofit.Builder()
        builder.baseUrl(BuildConfig.BaseURL)
        builder.addConverterFactory(GsonConverterFactory.create())
        if (BuildConfig.DEBUG) {
            builder.client(okHttpClient)
        }
        retrofit = builder.build()

        if (NetworkUtil.getConnectionStatus(mContext) === NetworkUtil.NOT_CONNECTED) {
            throw NoConnectionException(mContext!!.getString(R.string.no_iternet))
        }

        return retrofit.create(ApiInterface::class.java)
    }


}
