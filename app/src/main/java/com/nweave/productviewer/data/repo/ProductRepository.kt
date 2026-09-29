package com.nweave.productviewer.data.repo

import com.nweave.productviewer.data.local.FetchMethodStorage
import com.nweave.productviewer.data.model.Product
import com.nweave.productviewer.data.remote.HttpUrlConnectionFetcher
import com.nweave.productviewer.data.remote.RetrofitFetcher
import javax.inject.Inject
import javax.inject.Singleton

// Gives the screens the products. It only decides which fetcher to use;
// the saved method itself is kept by FetchMethodStorage.
@Singleton
class ProductRepository @Inject constructor(
    private val fetchMethodStorage: FetchMethodStorage,
    private val httpUrlConnectionFetcher: HttpUrlConnectionFetcher,
    private val retrofitFetcher: RetrofitFetcher
) {

    fun fetchProducts(onResult: (List<Product>?) -> Unit) {
        if (fetchMethodStorage.getFetchMethod() == FetchMethodStorage.METHOD_RETROFIT) {
            retrofitFetcher.fetchProducts(onResult)
        } else {
            httpUrlConnectionFetcher.fetchProducts(onResult)
        }
    }
}