package com.nweave.productviewer.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.nweave.productviewer.data.model.Product
import com.nweave.productviewer.databinding.ActivityProductDetailsBinding
import com.nweave.productviewer.ui.image.ProductImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var imageLoader: ProductImageLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // The product the list put in the Intent.
        val product = IntentCompat.getSerializableExtra(intent, EXTRA_PRODUCT, Product::class.java)
        if (product == null) {
            finish()
            return
        }

        // The toolbar with the back arrow.
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // The XML shows the name, price and description by itself.
        binding.product = product
        imageLoader.load(binding.productImage, product.imageUrl)
    }

    companion object {
        private const val EXTRA_PRODUCT = "product"

        // The only way to open this screen: it always carries a product.
        fun newIntent(context: Context, product: Product): Intent =
            Intent(context, ProductDetailsActivity::class.java)
                .putExtra(EXTRA_PRODUCT, product)
    }
}

