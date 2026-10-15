package com.nweave.productviewer.data.remote

import android.util.Log
import com.nweave.productviewer.data.model.Product
import javax.inject.Inject

// Fetches the products with Retrofit.
// @Inject constructor: Hilt passes in ProductsApi, which NetworkModule builds once.
class RetrofitFetcher @Inject constructor(private val api: ProductsApi) {

    // Returns null when the feed could not be read, for any reason.
    suspend fun fetchProducts(): List<Product>? =
        try {
            api.getFeaturedItems().map { it.product }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch products", e)
            null
        }

    companion object {
        private const val TAG = "Retrofit"
    }
}