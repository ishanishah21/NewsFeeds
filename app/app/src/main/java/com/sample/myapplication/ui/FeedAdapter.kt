package com.sample.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sample.myapplication.R
import com.sample.myapplication.databinding.ItemFeedBinding
import com.sample.myapplication.model.FeedData

import java.util.ArrayList

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    private var feedData: List<FeedData>? = null

    val itemCount: Int
        @Override
        get() = feedData!!.size()

    init {
        feedData = ArrayList()
    }

    fun setFeedData(feedData: List<FeedData>) {
        val previousSize = this.feedData!!.size()
        this.feedData!!.addAll(feedData)
        notifyItemRangeChanged(previousSize, feedData.size())
    }

    fun clearAdapterData() {
        feedData = ArrayList()
        notifyDataSetChanged()
    }

    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemFeedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_feed, parent, false)
        return FeedViewHolder(itemFeedBinding)
    }

    @Override
    fun onBindViewHolder(@NonNull holder: FeedViewHolder, position: Int) {
        holder.bindData(feedData!![position])
    }

    internal inner class FeedViewHolder private constructor(@param:NonNull private val itemView: ItemFeedBinding) : RecyclerView.ViewHolder(itemView.getRoot()) {

        fun bindData(feedData: FeedData) {
            itemView.setFeedData(feedData)
        }
    }

    companion object {

        @BindingAdapter("imageUrl")
        fun loadImage(imageView: ImageView, url: String?) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.mipmap.ic_launcher)
            requestOptions.error(R.mipmap.ic_launcher)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
            if (url != null && (url.contains("https://") || url.contains("http://"))) {
                Glide.with(imageView.getContext()).setDefaultRequestOptions(requestOptions).load(url).into(imageView)
            }
        }
    }
}
