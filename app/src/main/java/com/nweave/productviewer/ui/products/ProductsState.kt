package com.nweave.productviewer.ui.products

import com.nweave.productviewer.data.model.Product

sealed class ProductsState {
    data object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data object Error : ProductsState()
}