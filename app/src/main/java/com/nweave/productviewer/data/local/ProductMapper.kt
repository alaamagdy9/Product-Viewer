package com.nweave.productviewer.data.local

import com.nweave.productviewer.data.model.Product

// The table and the network model are allowed to drift apart, so the
// translation between them lives in one place.
fun ProductEntity.toProduct(): Product = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl
)