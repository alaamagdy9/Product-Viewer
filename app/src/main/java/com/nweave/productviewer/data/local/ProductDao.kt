package com.nweave.productviewer.data.local



import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// The one place that owns the SQL. Room writes the implementation.
@Dao
interface ProductDao {

    // A subscription, not a single read: every write to the table sends a
    // fresh list to whoever is collecting.
    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<ProductEntity>>

    // Existing id -> update that row. New id -> insert it.
    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    // Drops anything the feed no longer lists.
    @Query("DELETE FROM products WHERE id NOT IN (:ids)")
    suspend fun deleteNotIn(ids: List<Long>)
}
