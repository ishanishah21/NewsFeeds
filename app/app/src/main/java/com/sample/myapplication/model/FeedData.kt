package com.sample.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "feed_data")
class FeedData (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("imageHref")
    @Expose
    var imageHref: String? = null
)
