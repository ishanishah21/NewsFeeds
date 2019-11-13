package com.sample.myapplication.repository

import com.sample.myapplication.model.FeedData

interface OnResponse {
    fun onRowsUpdated(feedData: List<FeedData>)

    fun onTitleUpdated(title: String)

    fun onFailure(msg: String)
}