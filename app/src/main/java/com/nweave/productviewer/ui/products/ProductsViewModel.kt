package com.nweave.productviewer.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nweave.productviewer.R
import com.nweave.productviewer.data.model.Product
import com.nweave.productviewer.data.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableLiveData<ProductsState>(
        ProductsState.Loading(R.string.reading_from_storage)
    )
    val state: LiveData<ProductsState> = _state

    // The last list the database sent, and whether the most recent attempt at
    // the feed failed. The screen is worked out from these two.
    private var stored: List<Product> = emptyList()
    private var refreshFailed = false

    init {
        // Subscribed once. Every write to the table arrives here on its own,
        // so nothing ever has to ask the database for the products again.
        viewModelScope.launch {
            repository.getProducts().collect { products ->
                stored = products
                publish()
            }
        }
        refresh()
    }

    // Also what "Try again" calls.
    fun refresh() {
        refreshFailed = false
        if (stored.isEmpty()) {
            _state.value = ProductsState.Loading(R.string.parsing_json_feed)
        }
        viewModelScope.launch {
            refreshFailed = !repository.refresh()
            publish()
        }
    }

    // Stored products are shown whenever there are any, so the screen never
    // waits on the network. The error screen is only for a failed feed with
    // nothing saved to fall back on.
    private fun publish() {
        _state.value = when {
            stored.isNotEmpty() -> ProductsState.Success(stored, isStale = refreshFailed)
            refreshFailed -> ProductsState.Error
            else -> ProductsState.Loading(R.string.parsing_json_feed)
        }
    }
}