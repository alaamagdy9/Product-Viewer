package com.nweave.productviewer.data.remote

import android.util.Log
import com.google.gson.Gson
import com.nweave.productviewer.data.model.FeaturedItem
import com.nweave.productviewer.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URI
import javax.inject.Inject
class HttpUrlConnectionFetcher @Inject constructor(private val gson: Gson) {

    suspend fun fetchProducts(): List<Product>? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            connection = URI(PRODUCTS_URL).toURL().openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val json = connection.inputStream.bufferedReader().use { it.readText() }
            // Parsing with the Gson that Hilt gave us, instead of a new Gson() every time.
            gson.fromJson(json, Array<FeaturedItem>::class.java).map { it.product }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch products", e)
            null
        } finally {
            connection?.disconnect()
        }
    }

    companion object {
        private const val PRODUCTS_URL = "https://test.nweave.com/featured.txt"
        private const val TAG = "HttpUrlConnection"
    }
}
