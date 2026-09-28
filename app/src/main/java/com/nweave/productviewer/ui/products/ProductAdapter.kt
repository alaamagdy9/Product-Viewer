package com.nweave.productviewer.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nweave.productviewer.data.model.Product
import com.nweave.productviewer.databinding.ItemProductBinding
import com.nweave.productviewer.ui.details.ProductDetailsActivity

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // The ViewHolder keeps the binding (the generated ItemProductBinding) instead of each TextView.
    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Inflate item_product.xml through its binding class.
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }
// here's the position that i want you to insert the image in
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

      // The XML shows the name, price and description by itself.
        holder.binding.product = product
        holder.binding.executePendingBindings()
    // Tapping the cell opens the details screen for this product.
    holder.binding.productCard.setOnClickListener { view ->
        view.context.startActivity(ProductDetailsActivity.newIntent(view.context, product))
    }

        ProductImageTask.load(holder.binding.productImage, product.imageUrl)
    }

    override fun getItemCount(): Int = products.size
}