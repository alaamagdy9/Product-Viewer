package com.nweave.productviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// @HiltViewModel + @Inject constructor: Hilt creates the ViewModel and passes in ProductRepository.

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