package com.nweave.productviewer.ui.products

import com.nweave.productviewer.data.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllProductsFragment : BaseProductsFragment() {


    override fun sortProducts(products: List<Product>) = products

}