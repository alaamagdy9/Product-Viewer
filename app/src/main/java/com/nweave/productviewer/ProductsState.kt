package com.nweave.productviewer

sealed class ProductsState {
    data object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data object Error : ProductsState()
}