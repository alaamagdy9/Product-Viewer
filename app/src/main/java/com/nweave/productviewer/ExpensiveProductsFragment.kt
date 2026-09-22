package com.nweave.productviewer



import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensiveProductsFragment : BaseProductsFragment() {
    override fun sortProducts(products: List<Product>) = products.sortedByDescending { it.price }
}