package de.wackernagel.droidfridge.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.wackernagel.droidfridge.data.ProductPrice

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

    @Query("SELECT * FROM product_prices WHERE product_id = :productId ORDER BY created_at ASC")
    fun getAllProductPrices(productId: Long): LiveData<List<ProductPrice>>
}