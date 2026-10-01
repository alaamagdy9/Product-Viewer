package com.nweave.productviewer.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.nweave.productviewer.ui.main.MainActivity
import com.nweave.productviewer.R
import com.nweave.productviewer.data.local.FetchMethodStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.nweave.productviewer.data.local.ImageLoaderStorage

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    // Hilt fills this in (with the fetchstorage) during super.onCreate().
    @Inject
    lateinit var fetchMethodStorage: FetchMethodStorage
    // Hilt fills this in too. It remembers which image library to use.
    @Inject
    lateinit var imageLoaderStorage: ImageLoaderStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Every time the app opens, flip between HttpURLConnection and Retrofit and save it.
        fetchMethodStorage.switchFetchMethod()
        // And flip between Picasso and Universal Image Loader as well.
        imageLoaderStorage.switchImageLoader()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}