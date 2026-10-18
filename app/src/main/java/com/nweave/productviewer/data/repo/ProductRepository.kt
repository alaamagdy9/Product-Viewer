package com.nweave.productviewer.data.repo

import androidx.room.withTransaction
import com.nweave.productviewer.data.local.FetchMethodStorage
import com.nweave.productviewer.data.local.ProductDao
import com.nweave.productviewer.data.local.ProductDatabase
import com.nweave.productviewer.data.local.toEntity
import com.nweave.productviewer.data.local.toProduct
import com.nweave.productviewer.data.model.Product
import com.nweave.productviewer.data.remote.HttpUrlConnectionFetcher
import com.nweave.productviewer.data.remote.RetrofitFetcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class ProductRepository @Inject constructor(
    private val fetchMethodStorage: FetchMethodStorage,
    private val httpUrlConnectionFetcher: HttpUrlConnectionFetcher,
    private val retrofitFetcher: RetrofitFetcher,
    private val database: ProductDatabase,
    private val dao: ProductDao
) {

    // What the screen subscribes to. Room sends a new list after every write,
    // so nothing has to ask for the products a second time.
    fun getProducts(): Flow<List<Product>> =
        dao.getProducts().map { entities -> entities.map { it.toProduct() } }

    // Asks the feed for a fresher copy and stores it. Returns false when the
    // feed could not be reached, so the screen can say the list is a saved one.
    suspend fun refresh(): Boolean {
        val products = when (fetchMethodStorage.getFetchMethod()) {
            FetchMethodStorage.METHOD_RETROFIT -> retrofitFetcher.fetchProducts()
            else -> httpUrlConnectionFetcher.fetchProducts()
        } ?: return false

        // One transaction, so a collector never sees the table half written.
        database.withTransaction {
            dao.upsertAll(products.map { it.toEntity() })
            if (products.isNotEmpty()) {
                dao.deleteNotIn(products.map { it.id })
            }
        }
        return true
    }
}
