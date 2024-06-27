package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductPrice

@Dao
interface ProductPriceDao {
    @Insert
    suspend fun insert(productPrice: ProductPrice): Long

    @Update
    suspend fun update(productPrice: ProductPrice)

    @Delete
    suspend fun delete(productPrice: ProductPrice)

    @Query("SELECT * FROM product_prices WHERE id = :id")
    fun get(id: Long): LiveData<ProductPrice>

    @Query("SELECT * FROM product_prices WHERE product_id = :productId ORDER BY creationDate ASC")
    fun getAllProductPrices(productId: Long): LiveData<List<ProductPrice>>
}