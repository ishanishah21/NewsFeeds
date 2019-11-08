package com.sample.myapplication.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sample.myapplication.model.FeedData;

import java.util.List;

@Dao
public interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public List<Long> insertData(List<FeedData> feedData);

    @Query("SELECT * FROM feed_data")
    public List<FeedData> getData();

    @Query("SELECT COUNT(*) FROM feed_data")
    int getRecordCounts();

    @Query("DELETE FROM feed_data")
    void deleteAllFeeds();

}
