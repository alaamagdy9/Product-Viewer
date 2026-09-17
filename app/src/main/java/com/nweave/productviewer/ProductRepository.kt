package com.nweave.productviewer
import  android.content.Context
import androidx.core.content.edit

object ProductRepository {
    private const val PREFS_NAME = "product_viewer_prefs"
    private const val KEY_FETCH_METHOD = "fetch_method"
    const val METHOD_HTTP_URL_CONNECTION = "HttpURLConnection"
    const val METHOD_RETROFIT = "Retrofit"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun switchFetchMethod(context: Context) {
        val last = prefs(context).getString(KEY_FETCH_METHOD, null)
        val next = if (last == METHOD_HTTP_URL_CONNECTION) METHOD_RETROFIT else METHOD_HTTP_URL_CONNECTION
        prefs(context).edit { putString(KEY_FETCH_METHOD, next) }
    }
    fun getFetchMethod(context: Context): String =
        prefs(context).getString(KEY_FETCH_METHOD, METHOD_HTTP_URL_CONNECTION) ?: METHOD_HTTP_URL_CONNECTION
    fun fetchProducts(context: Context, onResult: (List<Product>?) -> Unit) {
        if (getFetchMethod(context) == METHOD_RETROFIT) {
            RetrofitFetcher.fetchProducts(onResult)
        } else {
            HttpUrlConnectionFetcher.fetchProducts(onResult)
        }
    }
}








