package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity( tableName = "products", indices = [Index(value=["name"], unique = true)] )
data class Product (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "alias")
    var alias: String = "",
    @ColumnInfo(name = "image_path")
    var imagePath: String = "",
)