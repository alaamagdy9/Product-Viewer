package com.nweave.productviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nweave.productviewer.databinding.ItemProductBinding

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // The ViewHolder keeps the binding (the generated ItemProductBinding) instead of each TextView.
    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Inflate item_product.xml through its binding class.
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        // Give the product to the layout. The XML shows the name and the price by itself.
        holder.binding.product = products[position]

        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = products.size
}