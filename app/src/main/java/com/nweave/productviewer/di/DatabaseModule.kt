package com.nweave.productviewer.di

import android.content.Context
import androidx.room.Room
import com.nweave.productviewer.data.local.ProductDao
import com.nweave.productviewer.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    //using application context so the app remains as long as the app runs
    fun provideDatabase(@ApplicationContext context: Context): ProductDatabase =
        Room.databaseBuilder(context, ProductDatabase::class.java, "product_viewer.db").build()
//hilt looks for the return type so here the return type provides
    //the productdatabase
    @Provides
    fun provideProductDao(database: ProductDatabase): ProductDao = database.productDao()
}