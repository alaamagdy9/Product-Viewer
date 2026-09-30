package com.nweave.productviewer.data.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Stores and reads which image library the app uses (Picasso or Universal Image Loader).

@Singleton
class ImageLoaderStorage @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Called from the Splash every time the app opens: flip the library and save it.
    fun switchImageLoader() {
        val last = prefs.getString(KEY_IMAGE_LOADER, null)
        val next = if (last == LOADER_PICASSO) LOADER_UNIVERSAL else LOADER_PICASSO
        prefs.edit { putString(KEY_IMAGE_LOADER, next) }
    }

    // Reads the saved library. If nothing is saved yet, it returns Picasso.
    fun getImageLoader(): String =
        prefs.getString(KEY_IMAGE_LOADER, LOADER_PICASSO) ?: LOADER_PICASSO

    companion object {
        private const val PREFS_NAME = "product_viewer_prefs"
        private const val KEY_IMAGE_LOADER = "image_loader"
        const val LOADER_PICASSO = "Picasso"
        const val LOADER_UNIVERSAL = "Universal Image Loader"
    }
}