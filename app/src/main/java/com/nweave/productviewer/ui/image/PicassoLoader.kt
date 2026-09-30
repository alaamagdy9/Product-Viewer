package com.nweave.productviewer.ui.image

import android.widget.ImageView
import com.squareup.picasso.Picasso
import javax.inject.Inject
import javax.inject.Singleton

// Loads an image with Picasso. Picasso keeps its own memory and disk caches and
// cancels the previous request for a recycled ImageView, so the bitmap cache and
// the tag check we wrote by hand for the AsyncTask are not needed here.
@Singleton
class PicassoLoader @Inject constructor() {

    fun load(imageView: ImageView, url: String) {
        Picasso.get()
            .load(url)
            .into(imageView)
    }
}