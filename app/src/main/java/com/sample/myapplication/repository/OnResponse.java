package com.sample.myapplication.repository;

import com.sample.myapplication.model.FeedData;

import java.util.List;

public interface OnResponse {
    void onRowsUpdated(List<FeedData> feedData);

    void onTitleUpdated(String title);

    void onFailure(String msg);
}