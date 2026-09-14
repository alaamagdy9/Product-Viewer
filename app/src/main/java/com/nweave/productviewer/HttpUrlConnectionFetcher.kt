package com.nweave.productviewer

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URI

object HttpUrlConnectionFetcher {

    private const val PRODUCTS_URL = "https://dummyjson.com/products"
    private const val TAG = "HttpUrlConnection"

    fun fetchProducts(onResult: (List<Product>?) -> Unit) {
        Thread {
            var products: List<Product>? = null
            var connection: HttpURLConnection? = null
            try {
                connection = URI(PRODUCTS_URL).toURL().openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val json = connection.inputStream.bufferedReader().use { it.readText() }
                products = Gson().fromJson(json, ProductsResponse::class.java).products
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch products", e)
            } finally {
                connection?.disconnect()
            }
            Handler(Looper.getMainLooper()).post { onResult(products) }
        }.start()
    }
}