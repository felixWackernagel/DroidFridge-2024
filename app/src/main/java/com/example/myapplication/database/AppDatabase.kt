package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
}