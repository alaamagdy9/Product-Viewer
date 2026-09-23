package com.nweave.productviewer


import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AllProductsFragment : BaseProductsFragment() {


    override fun sortProducts(products: List<Product>) = products

}