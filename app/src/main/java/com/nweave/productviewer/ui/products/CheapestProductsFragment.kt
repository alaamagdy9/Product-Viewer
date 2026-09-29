package com.nweave.productviewer.ui.products

import com.nweave.productviewer.data.model.Product
import dagger.hilt.android.AndroidEntryPoint

// Most Cheapest: lowest price first.
@AndroidEntryPoint

class CheapestProductsFragment : BaseProductsFragment() {
    override fun sortProducts(products: List<Product>) = products.sortedBy { it.price }
}