package com.nweave.productviewer.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nweave.productviewer.data.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableLiveData<ProductsState>()
    val state: LiveData<ProductsState> = _state

    // Called by every screen when its view is created. The products are fetched
    // only when there is nothing to show yet, so opening a product and coming
    // back does not start the same request again.
    fun loadProductsIfNeeded() {
        if (_state.value is ProductsState.Success) return
        loadProducts()
    }

    // Always fetches. Used by the "Try again" button, which must reach the server.
    fun loadProducts() {
        _state.value = ProductsState.Loading
        repository.fetchProducts { products ->
            _state.value = if (products != null) ProductsState.Success(products) else ProductsState.Error
        }
    }
}