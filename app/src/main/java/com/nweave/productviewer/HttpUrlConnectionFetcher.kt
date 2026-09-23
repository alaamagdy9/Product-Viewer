package com.nweave.productviewer

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URI
import javax.inject.Inject


// @Inject constructor: Hilt creates this class and passes in the app's single Gson (from NetworkModule).
class HttpUrlConnectionFetcher @Inject constructor(private val gson: Gson) {


    fun fetchProducts(onResult: (List<Product>?) -> Unit) {

        Thread {
            var products: List<Product>? = null
            var connection: HttpURLConnection? = null
            try {
                connection = URI(PRODUCTS_URL).toURL().openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val json = connection.inputStream.bufferedReader().use { it.readText() }
                // Parsing with the Gson that Hilt gave us, instead of a new Gson() every time.
                products = gson.fromJson(json, Array<FeaturedItem>::class.java).map { it.product }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch products", e)
            } finally {
                connection?.disconnect()
            }

            Handler(Looper.getMainLooper()).post { onResult(products) }
        }.start()
    }


    companion object {
        private const val PRODUCTS_URL = "https://test.nweave.com/featured.txt"
        private const val TAG = "HttpUrlConnection"
    }
}