package com.sample.myapplication.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sample.myapplication.R;
import com.sample.myapplication.databinding.ItemFeedBinding;
import com.sample.myapplication.model.FeedData;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private List<FeedData> feedData;


    public FeedAdapter() {
        feedData = new ArrayList<>();
    }

    public void setFeedData(List<FeedData> feedData) {
        int previousSize = this.feedData.size();
        this.feedData.addAll(feedData);
        notifyItemRangeChanged(previousSize, feedData.size());
    }

    public void clearAdapterData() {
        feedData = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedBinding itemFeedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_feed, parent, false);
        return new FeedViewHolder(itemFeedBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.bindData(feedData.get(position));
    }

    @Override
    public int getItemCount() {
        return feedData.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        private ItemFeedBinding itemView;

        private FeedViewHolder(@NonNull ItemFeedBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bindData(FeedData feedData) {
            itemView.setFeedData(feedData);
        }
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        if (url != null && (url.contains("https://") || url.contains("http://"))) {
            Glide.with(imageView.getContext()).setDefaultRequestOptions(requestOptions).load(url).into(imageView);
        }
    }
}
