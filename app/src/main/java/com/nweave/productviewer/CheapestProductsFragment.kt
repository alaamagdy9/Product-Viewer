package com.nweave.productviewer


class CheapestProductsFragment : BaseProductsFragment() {
    override fun sortProducts(products: List<Product>) = products.sortedBy { it.price }
}