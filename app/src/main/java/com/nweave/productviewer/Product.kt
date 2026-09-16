package com.nweave.productviewer

data class Product(
    // "name" and "price" have the same names in the JSON, so no @SerializedName is needed.

    val name: String,
    val price: Double,)