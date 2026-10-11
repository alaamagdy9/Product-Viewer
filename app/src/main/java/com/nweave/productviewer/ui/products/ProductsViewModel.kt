package com.nweave.productviewer.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nweave.productviewer.R
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
//parse the feed into the database,
// then show what the database gives back. Used by "Try again" as well.
    fun loadProducts() {
        _state.value = ProductsState.Loading(R.string.parsing_json_feed)

        repository.fetchAndStore { stored ->
            if (!stored) {
                _state.value = ProductsState.Error
                return@fetchAndStore
            }

            _state.value = ProductsState.Loading(R.string.reading_from_storage)

            repository.readStoredProducts { products ->
                _state.value = ProductsState.Success(products)
            }
        }
    }
}
