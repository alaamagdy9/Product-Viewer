package com.nweave.productviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheapestProductsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_products, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Same as AllProductsFragment
        ProductRepository.fetchProducts(requireContext()) { products ->
            if (products != null) {

                recyclerView.adapter = ProductAdapter(products.sortedBy { it.price })
            } else {
                Toast.makeText(view.context, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}