package com.nweave.productviewer.ui.products

import androidx.annotation.StringRes
import com.nweave.productviewer.data.model.Product

sealed class ProductsState {

    // Loading now carries which step it is on, so the screen can name it.
    data class Loading(@StringRes val messageRes: Int) : ProductsState()

    data class Success(val products: List<Product>) : ProductsState()

    data object Error : ProductsState()
}