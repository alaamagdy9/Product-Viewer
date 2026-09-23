package com.nweave.productviewer.data.remote

import com.nweave.productviewer.data.model.FeaturedItem
import retrofit2.Call
import retrofit2.http.GET

interface ProductsApi {
    @GET("featured.txt")
    fun getFeaturedItems(): Call<List<FeaturedItem>>
}