package com.nweave.productviewer.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nweave.productviewer.data.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// Keeps the products in an SQLite table on the device, so they are still there
// the next time the app opens, with or without a network.
@Singleton
class ProductsDbHelper @Inject constructor(@ApplicationContext context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Called once, the first time the database file is created.
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE (
                $COLUMN_NAME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_IMAGE_URL TEXT
            )
            """
        )
    }
    // Called when DATABASE_VERSION goes up. The products are only a copy of what
    // the server has, so the simplest upgrade is to drop them and fetch again.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }
    // Replaces whatever is stored with the products that were just parsed.
    fun replaceAll(products: List<Product>) {
        val db = writableDatabase
        db.delete(TABLE, null, null)
        products.forEach { product ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, product.name)
                put(COLUMN_DESCRIPTION, product.description)
                put(COLUMN_PRICE, product.price)
                put(COLUMN_IMAGE_URL, product.imageUrl)
            }
            db.insert(TABLE, null, values)
        }
    }
    // Reads every stored product back. Columns are looked up by name, so the
    // order they were declared in cannot silently break this.
    fun readAll(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor = readableDatabase.query(TABLE, null, null, null, null, null, null)
        cursor.use {
            val nameIndex = it.getColumnIndexOrThrow(COLUMN_NAME)
            val descriptionIndex = it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
            val priceIndex = it.getColumnIndexOrThrow(COLUMN_PRICE)
            val imageUrlIndex = it.getColumnIndexOrThrow(COLUMN_IMAGE_URL)

            while (it.moveToNext()) {
                products.add(
                    Product(
                        name = it.getString(nameIndex),
                        description = it.getString(descriptionIndex),
                        price = it.getDouble(priceIndex),
                        imageUrl = it.getString(imageUrlIndex)
                    )
                )
            }
        }
        return products
    }

    companion object {
        private const val DATABASE_NAME = "products.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE = "products"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_IMAGE_URL = "image_url"
    }
}