package com.nweave.productviewer.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nweave.productviewer.R
import com.nweave.productviewer.data.model.Product

abstract class BaseProductsFragment : Fragment()       //the cheapest and expensive will inherit
{
    private val viewModel: ProductsViewModel by viewModels()
    abstract fun sortProducts(products: List<Product>): List<Product>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_products, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val errorView: View = view.findViewById(R.id.errorView)
        val retryButton: Button = view.findViewById(R.id.retryButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // "Try again" asks the ViewModel to load the products again.
        retryButton.setOnClickListener { viewModel.loadProducts() }

        // Show only the view that matches the state.
        viewModel.state.observe(viewLifecycleOwner) { state ->
            progressBar.isVisible = state is ProductsState.Loading
            errorView.isVisible = state is ProductsState.Error
            recyclerView.isVisible = state is ProductsState.Success

            if (state is ProductsState.Success) {
                // Each screen sorts the products its own way before showing them.
                recyclerView.adapter = ProductAdapter(sortProducts(state.products))
            }
        }
    }
}