package com.sample.myapplication

import android.app.Application
import androidx.room.Room
import com.sample.myapplication.db.FeedDatabase

class Application : Application() {
    var feedDatabase: FeedDatabase? = null
    override fun onCreate() {
        super.onCreate()
        feedDatabase = Room.databaseBuilder(this, FeedDatabase::class.java, "FeedDb.db").build()

    }
}