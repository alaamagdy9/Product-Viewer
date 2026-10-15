package com.nweave.productviewer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// How a product is stored. Kept separate from the Product the app works
// with, so a change to the table does not reach the rest of the code.
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)