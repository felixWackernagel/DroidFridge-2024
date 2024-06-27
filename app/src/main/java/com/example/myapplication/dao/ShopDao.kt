package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.Shop

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

    @Query("SELECT * FROM shops")
    fun getAllShops(): LiveData<List<Shop>>
}