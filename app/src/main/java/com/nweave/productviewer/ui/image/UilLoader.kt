package com.nweave.productviewer.ui.image

import android.content.Context
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Loads an image with Universal Image Loader

@Singleton
class UilLoader @Inject constructor(@ApplicationContext context: Context) {

    private val imageLoader: ImageLoader = ImageLoader.getInstance()

    init {
        if (!imageLoader.isInited) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context))
        }
    }

    fun load(imageView: ImageView, url: String) {
        imageLoader.displayImage(url, imageView)
    }
}