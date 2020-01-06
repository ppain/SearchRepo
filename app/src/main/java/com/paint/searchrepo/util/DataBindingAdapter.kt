package com.paint.searchrepo.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.paint.searchrepo.R

object DataBindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(imageView: ImageView, imageURL: String?) {
        Glide.with(imageView.context)
            .setDefaultRequestOptions(
                RequestOptions()
            )
            .load(imageURL)
            .thumbnail(0.5f)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.logo_github)
            .into(imageView)
    }
}