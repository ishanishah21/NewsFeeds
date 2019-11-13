package com.sample.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sample.myapplication.R
import com.sample.myapplication.databinding.ItemFeedBinding
import com.sample.myapplication.model.FeedData
import kotlinx.android.synthetic.main.item_feed.view.*

import java.util.ArrayList

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    override fun getItemCount(): Int {
        return feedData!!.size
    }

    private var feedData: ArrayList<FeedData>? = null


    init {
        feedData = ArrayList()
    }

    fun setFeedData(feedData: List<FeedData>) {
        val previousSize = this.feedData!!.size
        this.feedData!!.addAll(feedData)
        notifyItemRangeChanged(previousSize, feedData.size)
    }

    fun clearAdapterData() {
        feedData = ArrayList()
        notifyDataSetChanged()
    }

    @NonNull
    override
    fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemFeedBinding: ItemFeedBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_feed, parent, false)
        return FeedViewHolder(itemFeedBinding)
    }

    override
    fun onBindViewHolder(@NonNull holder: FeedViewHolder, position: Int) {
        holder.bindData(feedData!![position])
    }

    inner class FeedViewHolder constructor(val itemViews: ItemFeedBinding) : RecyclerView.ViewHolder(itemViews.root) {

        fun bindData(feedData: FeedData) {
            itemViews.feedData = feedData
        }
    }

    /*    @BindingAdapter("imageUrl")
        fun ImageView.setImageUrl(url: String?) {
            Glide.with(context).load(url).into(this)
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.mipmap.ic_launcher)
            requestOptions.error(R.mipmap.ic_launcher)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
            if (url != null && (url.contains("https://") || url.contains("http://"))) {
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(url).into(this)
            }
        }*/
    object ImageBindingAdapter {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(view: ImageView, url: String?) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.mipmap.ic_launcher)
            requestOptions.error(R.mipmap.ic_launcher)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
            if (url != null && (url.contains("https://") || url.contains("http://"))) {
                Glide.with(view.context).setDefaultRequestOptions(requestOptions).load(url).into(view)
            }
        }
    }
}
