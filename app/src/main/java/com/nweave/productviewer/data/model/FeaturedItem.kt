package com.nweave.productviewer.data.model

import com.google.gson.annotations.SerializedName

data class FeaturedItem(

    @SerializedName("Product") val product: Product
)