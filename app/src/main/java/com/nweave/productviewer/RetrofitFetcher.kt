package com.nweave.productviewer

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitFetcher {

    private const val BASE_URL = "https://test.nweave.com/"
    private const val TAG = "Retrofit"

    private val api: ProductsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductsApi::class.java)

    fun fetchProducts(onResult: (List<Product>?) -> Unit) {
        api.getFeaturedItems().enqueue(object : Callback<List<FeaturedItem>> {

            override fun onResponse(
                call: Call<List<FeaturedItem>>,
                response: Response<List<FeaturedItem>>
            ) {
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
}
