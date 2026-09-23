package com.nweave.productviewer
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// The "recipes" Hilt uses to create objects from libraries (Gson and Retrofit),
// @InstallIn(SingletonComponent::class): these objects live as long as the app.
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://test.nweave.com/"

    // One Gson for the whole app (used by Retrofit and by HttpUrlConnectionFetcher).
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    // One Retrofit for the whole app. Hilt passes in the Gson from provideGson().
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    @Provides
    @Singleton
    fun provideProductsApi(retrofit: Retrofit): ProductsApi = retrofit.create(ProductsApi::class.java)
}
