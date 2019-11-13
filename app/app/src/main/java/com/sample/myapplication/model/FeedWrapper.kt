package com.sample.myapplication.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "feed_master")
class FeedWrapper (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("rows")
    @Expose
    @Ignore
    var rows: List<FeedData>? = null
)
