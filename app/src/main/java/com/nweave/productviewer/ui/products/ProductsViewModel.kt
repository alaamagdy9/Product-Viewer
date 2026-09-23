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

    // Runs once
    init {
        loadProducts()
    }


    fun loadProducts() {
        _state.value = ProductsState.Loading
        repository.fetchProducts { products ->
            _state.value = if (products != null) ProductsState.Success(products) else ProductsState.Error
        }
    }
}