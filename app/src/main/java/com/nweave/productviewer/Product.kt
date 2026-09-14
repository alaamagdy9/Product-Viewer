package com.nweave.productviewer

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("title") val name: String,
    val price: Double
)                //because the json file is TITLE so we changed from name to title