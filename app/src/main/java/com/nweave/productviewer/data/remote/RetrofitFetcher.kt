package com.nweave.productviewer.data.remote

import android.util.Log
import com.nweave.productviewer.data.model.FeaturedItem
import com.nweave.productviewer.data.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// Fetches the products with Retrofit.
// @Inject constructor: Hilt passes in ProductsApi, which NetworkModule builds once,
// so this class doesn't build Retrofit anymore.
class RetrofitFetcher @Inject constructor(private val api: ProductsApi) {

    fun fetchProducts(onResult: (List<Product>?) -> Unit) {
        api.getFeaturedItems().enqueue(object : Callback<List<FeaturedItem>> {
            override fun onResponse(call: Call<List<FeaturedItem>>, response: Response<List<FeaturedItem>>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.map { it.product })
                } else {
                    Log.e(TAG, "Failed to fetch products: HTTP ${response.code()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<FeaturedItem>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch products", t)
                onResult(null)
            }
        })
    }

    companion object {
        private const val TAG = "Retrofit"
    }
}