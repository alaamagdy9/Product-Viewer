package com.nweave.productviewer.ui.image

import android.widget.ImageView
import com.nweave.productviewer.data.local.ImageLoaderStorage
import javax.inject.Inject
import javax.inject.Singleton

// Gives the screens their images. It only decides which library to use;
// the saved choice itself is kept by ImageLoaderStorage.
@Singleton
class ProductImageLoader @Inject constructor(
    private val imageLoaderStorage: ImageLoaderStorage,
    private val picassoLoader: PicassoLoader,
    private val uilLoader: UilLoader
) {

    fun load(imageView: ImageView, url: String) {
        if (imageLoaderStorage.getImageLoader() == ImageLoaderStorage.LOADER_PICASSO) {
            picassoLoader.load(imageView, url)
        } else {
            uilLoader.load(imageView, url)
        }
    }
}