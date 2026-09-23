package com.nweave.productviewer.data.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Stores and reads which fetch method the app uses (HttpURLConnection or Retrofit).
// This is the only class that touches SharedPreferences instead of product repo so now the product repo only gets the products
@Singleton
class FetchMethodStorage @Inject constructor(@ApplicationContext context: Context) {

    // The SharedPreferences file, opened once with the Context Hilt gave us.
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Called from the Splash every time the app opens: flip the method and save it.
    fun switchFetchMethod() {
        val last = prefs.getString(KEY_FETCH_METHOD, null)
        val next = if (last == METHOD_HTTP_URL_CONNECTION) METHOD_RETROFIT else METHOD_HTTP_URL_CONNECTION
        prefs.edit { putString(KEY_FETCH_METHOD, next) }
    }

    // Reads the saved method. If nothing is saved yet, it returns HttpURLConnection.
    fun getFetchMethod(): String =
        prefs.getString(KEY_FETCH_METHOD, METHOD_HTTP_URL_CONNECTION) ?: METHOD_HTTP_URL_CONNECTION

    companion object {
        private const val PREFS_NAME = "product_viewer_prefs"
        private const val KEY_FETCH_METHOD = "fetch_method"
        const val METHOD_HTTP_URL_CONNECTION = "HttpURLConnection"
        const val METHOD_RETROFIT = "Retrofit"
    }
}