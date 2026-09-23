package com.nweave.productviewer.ui.products

import com.nweave.productviewer.data.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensiveProductsFragment : BaseProductsFragment() {
    override fun sortProducts(products: List<Product>) = products.sortedByDescending { it.price }
}