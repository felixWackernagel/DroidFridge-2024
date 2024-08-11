package de.wackernagel.droidfridge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.wackernagel.droidfridge.dao.ProductDao
import de.wackernagel.droidfridge.dao.ProductPriceDao
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.data.Product
import de.wackernagel.droidfridge.data.ProductPrice
import de.wackernagel.droidfridge.data.Shop

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
}