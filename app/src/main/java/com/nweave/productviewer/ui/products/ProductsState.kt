package com.nweave.productviewer.ui.products

import androidx.annotation.StringRes
import com.nweave.productviewer.data.model.Product

sealed class ProductsState {

    // Loading now carries which step it is on, so the screen can name it.
    data class Loading(@StringRes val messageRes: Int) : ProductsState()

    // isStale is true when the feed could not be reached and these products
    // came from the database rather than a fresh request.
    data class Success(
        val products: List<Product>,
        val isStale: Boolean = false
    ) : ProductsState()


    data object Error : ProductsState()
}