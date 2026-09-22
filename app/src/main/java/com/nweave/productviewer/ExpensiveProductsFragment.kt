package com.nweave.productviewer

class ExpensiveProductsFragment : BaseProductsFragment() {
    override fun sortProducts(products: List<Product>) = products.sortedByDescending { it.price }
}