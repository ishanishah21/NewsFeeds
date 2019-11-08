package com.sample.myapplication;


import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.ActivityTestRule;

import com.sample.myapplication.model.FeedData;
import com.sample.myapplication.ui.MainActivity;
import com.sample.myapplication.viewmodel.FeedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class MainActivityIntrumentTest {

    private FeedViewModel feedViewModel;
    private MutableLiveData<List<FeedData>> data = new MutableLiveData<>();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void validateEditText() {

    }

    @Before
    public void setup(){

    }


}
