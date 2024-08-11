package de.wackernagel.droidfridge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "shops" )
data class Shop (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "address")
    var address: String? = null,
    @ColumnInfo(name = "opening_hours")
    var openingHours: String? = null,
    @ColumnInfo(name = "phone")
    var phone: String? = null,
    @ColumnInfo(name = "image_path")
    var imagePath: String? = null,
)