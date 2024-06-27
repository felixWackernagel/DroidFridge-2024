package com.example.myapplication.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.MyApplication
import com.example.myapplication.dao.ProductDao
import com.example.myapplication.dao.ProductPriceDao
import com.example.myapplication.dao.ShopDao
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductPrice
import com.example.myapplication.data.Shop

@Database(
    entities = [Product::class, ProductPrice::class, Shop::class],
    exportSchema = true,
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val productDao: ProductDao
    abstract val productPriceDao: ProductPriceDao
    abstract val shopDao: ShopDao

    companion object {
        val TAG = this::class.simpleName

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    Log.d(TAG, "Database gets created ...")
                    instance = createInstance(context)
                    INSTANCE = instance
                }
                return instance
            }
        }

        // found at https://anadea.info/blog/how-to-pre-populate-android-room-database-on-first-application-launch
        private fun createInstance(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, MyApplication.APP_DB_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Thread { prepopulateDb(context, getInstance(context)) }.start()
                    }
                })
                .build()

        private fun prepopulateDb(context: Context, db: AppDatabase) {
            db.runInTransaction( DatabaseInitialization(context, db))
        }
    }
}