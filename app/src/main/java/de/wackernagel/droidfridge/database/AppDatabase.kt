package de.wackernagel.droidfridge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.wackernagel.droidfridge.dao.OpeningHoursDao
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.data.OpeningHours
import de.wackernagel.droidfridge.data.Shop

@Database(
    entities = [Shop::class, OpeningHours::class],
    exportSchema = true,
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val shopDao: ShopDao
    abstract val openingHours: OpeningHoursDao
}