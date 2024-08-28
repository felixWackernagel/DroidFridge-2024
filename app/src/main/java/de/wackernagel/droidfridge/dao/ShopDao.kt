package de.wackernagel.droidfridge.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.data.ShopWithOpeningHours

@Dao
interface ShopDao {
    @Insert
    suspend fun insert(shop: Shop): Long

    @Update
    suspend fun update(shop: Shop)

    @Delete
    suspend fun delete(shop: Shop)

    @Query("SELECT * FROM shops WHERE id = :id")
    fun get(id: Long): LiveData<Shop>

    @Transaction
    @Query("SELECT * FROM shops")
    fun getShopsWithOpeningHours(): LiveData<List<ShopWithOpeningHours>>
}