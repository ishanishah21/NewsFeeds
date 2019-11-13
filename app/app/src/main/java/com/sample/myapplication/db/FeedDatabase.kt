package com.sample.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase

import com.sample.myapplication.db.dao.FeedDao
import com.sample.myapplication.model.FeedData
import com.sample.myapplication.model.FeedWrapper

@Database(entities = { FeedWrapper.class, FeedData.class }, version = 1, exportSchema = true)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDataDao(): FeedDao
}
