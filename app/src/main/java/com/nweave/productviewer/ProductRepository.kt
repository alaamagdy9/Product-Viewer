package com.nweave.productviewer

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class ProductRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val httpUrlConnectionFetcher: HttpUrlConnectionFetcher,
    private val retrofitFetcher: RetrofitFetcher
) {

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

    // Fetches the products with the saved method.
    fun fetchProducts(onResult: (List<Product>?) -> Unit) {
        if (getFetchMethod() == METHOD_RETROFIT) {
            retrofitFetcher.fetchProducts(onResult)
        } else {
            httpUrlConnectionFetcher.fetchProducts(onResult)
        }
    }

    companion object {
        private const val PREFS_NAME = "product_viewer_prefs"
        private const val KEY_FETCH_METHOD = "fetch_method"
        const val METHOD_HTTP_URL_CONNECTION = "HttpURLConnection"
        const val METHOD_RETROFIT = "Retrofit"
    }
}