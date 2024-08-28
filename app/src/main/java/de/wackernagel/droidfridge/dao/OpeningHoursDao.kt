package de.wackernagel.droidfridge.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.wackernagel.droidfridge.data.OpeningHours

@Dao
interface OpeningHoursDao {
    @Insert
    suspend fun insert(openingHours: OpeningHours): Long

    @Update
    suspend fun update(openingHours: OpeningHours)

    @Delete
    suspend fun delete(openingHours: OpeningHours)

    @Query("SELECT * FROM opening_hours WHERE id = :id")
    fun get(id: Long): LiveData<OpeningHours>

    @Query("SELECT * FROM opening_hours WHERE shop_id = :id")
    fun getOpeningHoursForShop(id: Long ): LiveData<List<OpeningHours>>
}