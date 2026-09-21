package com.nweave.productviewer
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProductsViewModel (application: Application) : AndroidViewModel(application)  {
    private val _state = MutableLiveData<ProductsState>()
    val state: LiveData<ProductsState> = _state

    //work only once
    init {
        loadProducts()
    }
    fun loadProducts() {
        _state.value = ProductsState.Loading
        ProductRepository.fetchProducts(getApplication<Application>()) { products ->
            // Both fetchers call back on the main thread, so the state can be set directly.
            _state.value = if (products != null) ProductsState.Success(products) else ProductsState.Error
        }
    }
}