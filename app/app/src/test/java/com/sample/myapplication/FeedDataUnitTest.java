package com.sample.myapplication;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.sample.myapplication.db.FeedDatabase;
import com.sample.myapplication.model.FeedData;
import com.sample.myapplication.repository.FeedRepository;
import com.sample.myapplication.ui.MainActivity;
import com.sample.myapplication.viewmodel.FeedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FeedDataUnitTest {
    private FeedViewModel feedViewModel;
    private FeedRepository feedRepository;
    private FeedDatabase feedDatabase;
    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();


    @Before
    public void init() {
        Context mContext = Robolectric.buildActivity(MainActivity.class).get();
        feedDatabase = Room.inMemoryDatabaseBuilder(mContext, FeedDatabase.class).build();


        feedViewModel = new FeedViewModel();
        feedRepository = mock(FeedRepository.class);
        feedDatabase = mock(FeedDatabase.class);
        feedViewModel.setFeedDatabase(feedDatabase);
    }

    @Test
    public void dataAssertion() {
        Observer<List<FeedData>> observer = mock(Observer.class);
        feedViewModel.getFeedWrapperMutableLiveData().observeForever(observer);
        feedViewModel.getFacts();

    }

}
