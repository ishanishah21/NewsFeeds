package com.sample.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.sample.myapplication.model.FeedData

@Dao
interface FeedDao {

    @get:Query("SELECT * FROM feed_data")
    val data: List<FeedData>

    @get:Query("SELECT COUNT(*) FROM feed_data")
    val recordCounts: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(feedData: List<FeedData>): List<Long>

    @Query("DELETE FROM feed_data")
    fun deleteAllFeeds()

}
