package com.nweave.productviewer
import retrofit2.Call
import retrofit2.http.GET


interface ProductsApi {
    @GET("featured.txt")
    fun getFeaturedItems(): Call<List<FeaturedItem>>
}