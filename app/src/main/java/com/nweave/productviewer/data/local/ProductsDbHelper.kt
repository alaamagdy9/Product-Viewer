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

    // Called once, the first time the database file is created. The feed gives
    // every product an id, so that is the primary key: it is what lets a later
    // fetch update a product in place instead of inserting it again.
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_IMAGE_URL TEXT
            )
            """
        )
    }

    // Dropping and recreating is right only for this one bump: the version 1
    // rows have no id at all, so there is nothing to carry them over to, and
    // SQLite cannot add a PRIMARY KEY column with ALTER TABLE. The table is a
    // cache of the feed, so it refills on the next launch. From here on,
    // upgrades should migrate with ALTER TABLE rather than drop the table.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    // Writes the products the feed just returned. Rows are matched on the
    // product id, so a product that is already stored is updated in place
    // instead of being deleted and inserted again.
    fun saveAll(products: List<Product>) {
        val db = writableDatabase
        // One transaction instead of one disk write per product: it is far
        // faster, and a crash half way through leaves the table untouched
        // rather than half written.
        db.beginTransaction()
        try {
            products.forEach { product ->
                val values = ContentValues().apply {
                    put(COLUMN_ID, product.id)
                    put(COLUMN_NAME, product.name)
                    put(COLUMN_DESCRIPTION, product.description)
                    put(COLUMN_PRICE, product.price)
                    put(COLUMN_IMAGE_URL, product.imageUrl)
                }
                // Existing id -> update that row. New id -> insert it.
                db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }

            // Anything the feed no longer lists is dropped, so the table stays
            // an exact mirror of the feed. The ids come from our own parsing
            // and are numbers, so there is nothing to escape here.
            val ids = products.joinToString(",") { it.id.toString() }
            if (ids.isNotEmpty()) {
                db.delete(TABLE, "$COLUMN_ID NOT IN ($ids)", null)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // Reads every stored product back. Columns are looked up by name, so the
    // order they were declared in cannot silently break this.
    fun readAll(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor = readableDatabase.query(TABLE, null, null, null, null, null, null)
        cursor.use {
            val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
            val nameIndex = it.getColumnIndexOrThrow(COLUMN_NAME)
            val descriptionIndex = it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
            val priceIndex = it.getColumnIndexOrThrow(COLUMN_PRICE)
            val imageUrlIndex = it.getColumnIndexOrThrow(COLUMN_IMAGE_URL)

            while (it.moveToNext()) {
                products.add(
                    Product(
                        id = it.getLong(idIndex),
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
        private const val DATABASE_VERSION = 2

        private const val TABLE = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_IMAGE_URL = "image_url"
    }
}
