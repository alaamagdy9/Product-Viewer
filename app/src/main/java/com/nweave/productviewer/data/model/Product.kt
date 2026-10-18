package com.nweave.productviewer.data.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class Product(

    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    @SerializedName("image_url") val imageUrl: String
) : Serializable