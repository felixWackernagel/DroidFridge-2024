package de.wackernagel.droidfridge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "opening_hours",
    foreignKeys = [
        ForeignKey(
            entity = Shop::class,
            parentColumns = [ "id" ],
            childColumns = [ "shop_id" ],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [ "shop_id", "day" ])
    ]
)
data class OpeningHours(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "shop_id")
    var shopId: Long,
    @ColumnInfo(name = "start")
    var start: String,
    @ColumnInfo(name = "end")
    var end: String,
    @ColumnInfo(name = "day")
    var day: Int
) {
    /**
     * Returns formatted time range string (e.g. "08:00 - 20:00").
     */
    val timeRange: String
        get() = "$start - $end"
}
