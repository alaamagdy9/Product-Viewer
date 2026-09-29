package com.nweave.productviewer.ui.products

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URI

// AsyncTask is deprecated; used only because the assignment asks for it
@Suppress("DEPRECATION")
class ProductImageTask(imageView: ImageView, private val url: String) :
    AsyncTask<Void, Void, Bitmap?>() {

    // A weak reference: if the screen closes while the image is downloading,
    // the ImageView can still be freed instead of being kept alive by this task.
    private val imageViewRef = WeakReference(imageView)

    // Runs on a background thread: download the bytes and turn them into a Bitmap.
    override fun doInBackground(vararg params: Void?): Bitmap? {
        var connection: HttpURLConnection? = null
        return try {
            connection = URI(url).toURL().openConnection() as HttpURLConnection
            // The images are 800x800 but the cell is small, so decode them at half size
            // to use a quarter of the memory.
            val options = BitmapFactory.Options().apply { inSampleSize = 2 }
            val bitmap = connection.inputStream.use { BitmapFactory.decodeStream(it, null, options) }
            if (bitmap != null) cache[url] = bitmap
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load image: $url", e)
            null
        } finally {
            connection?.disconnect()
        }
    }

    // Runs on the main thread when the download finishes.
    override fun onPostExecute(result: Bitmap?) {
        val imageView = imageViewRef.get() ?: return
        // While we were downloading, the RecyclerView may have reused this row for another
        // product, so only set the image if this ImageView still wants this URL.
        if (result != null && imageView.tag == url) {
            imageView.setImageBitmap(result)
        }
    }

    companion object {
        private const val TAG = "ProductImageTask"

        // Images already downloaded, so scrolling back doesn't download them again.
        private val cache = HashMap<String, Bitmap>()

        // Called from the adapter for every row.
        fun load(imageView: ImageView, url: String) {
            imageView.tag = url
            val cached = cache[url]
            if (cached != null) {
                imageView.setImageBitmap(cached)
            } else {
                // Clear the previous product's image from the reused row.
                imageView.setImageDrawable(null)
                ProductImageTask(imageView, url).executeOnExecutor(THREAD_POOL_EXECUTOR)
            }
        }
    }
}