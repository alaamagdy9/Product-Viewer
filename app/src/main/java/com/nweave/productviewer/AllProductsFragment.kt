package com.nweave.productviewer

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

class AllProductsFragment : Fragment() {

    // The fragment's ViewModel. "by viewModels()" creates it the first time,
    // and gives back the same one after the phone rotates.
    private val viewModel: ProductsViewModel by viewModels() //to not create it from start everytime


    override fun onCreateView(  //create the layout
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

        // Called now with the current state, and again every time the state changes.
        viewModel.state.observe(viewLifecycleOwner) { state ->
            // Show only the view that matches the state, and hide the others.
            progressBar.isVisible = state is ProductsState.Loading
            errorView.isVisible = state is ProductsState.Error
            recyclerView.isVisible = state is ProductsState.Success

            if (state is ProductsState.Success) {
                recyclerView.adapter = ProductAdapter(state.products) //if sucess we'll put the products in the adapter
            }
        }
    }
}