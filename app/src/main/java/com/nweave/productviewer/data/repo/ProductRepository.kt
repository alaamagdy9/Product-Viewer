package com.nweave.productviewer.data.repo

import android.os.Handler
import android.os.Looper
import com.nweave.productviewer.data.local.FetchMethodStorage
import com.nweave.productviewer.data.local.ProductsDbHelper
import com.nweave.productviewer.data.model.Product
import com.nweave.productviewer.data.remote.HttpUrlConnectionFetcher
import com.nweave.productviewer.data.remote.RetrofitFetcher
import javax.inject.Inject
import javax.inject.Singleton


// Gives the screens the products. It decides which fetcher to use and keeps the
// parsed products in SQLite; the screens read them back from there.
@Singleton
class ProductRepository @Inject constructor(
    private val fetchMethodStorage: FetchMethodStorage,
    private val httpUrlConnectionFetcher: HttpUrlConnectionFetcher,
    private val retrofitFetcher: RetrofitFetcher,
    private val productsDbHelper: ProductsDbHelper
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    // Downloads and parses the feed, then stores it. Reports whether it worked,
    // on the main thread.
    fun fetchAndStore(onDone: (Boolean) -> Unit) {
        val fetch: ((List<Product>?) -> Unit) -> Unit = { callback ->
            if (fetchMethodStorage.getFetchMethod() == FetchMethodStorage.METHOD_RETROFIT) {
                retrofitFetcher.fetchProducts(callback)
            } else {
                httpUrlConnectionFetcher.fetchProducts(callback)
            }
        }
        fetch { products ->
            if (products == null) {
                onDone(false)
            } else {
                // Writing to a database is disk work, so it is kept off the main thread.
                Thread {
                    productsDbHelper.saveAll(products)
                    mainHandler.post { onDone(true) }
                }.start()
            }
        }
    }
    // Reads back whatever is stored. Also disk work, also off the main thread.
    fun readStoredProducts(onResult: (List<Product>) -> Unit) {
        Thread {
            val products = productsDbHelper.readAll()
            mainHandler.post { onResult(products) }
        }.start()
    }
}