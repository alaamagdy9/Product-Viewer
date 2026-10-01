package com.nweave.productviewer.ui.image

import android.content.Context
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.nostra13.universalimageloader.core.DisplayImageOptions

// Loads an image with Universal Image Loader

@Singleton
class UilLoader @Inject constructor(@ApplicationContext context: Context) {

    private val imageLoader: ImageLoader = ImageLoader.getInstance()
    // createDefault() leaves caching off, so it is asked for explicitly.
    // Picasso does both by default; this makes the two libraries comparable.
    private val options: DisplayImageOptions = DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .build()
    init {
        if (!imageLoader.isInited) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context))
        }
    }

    fun load(imageView: ImageView, url: String) {
        imageLoader.displayImage(url, imageView, options)
    }


}