package de.wackernagel.droidfridge.data

import java.text.DateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "product_prices",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = [ "id" ],
            childColumns = [ "product_id" ]
        )
    ],
    indices = [
        Index(
            value= [ "product_id" ]
        )
    ]
)
data class ProductPrice(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "product_id")
    var productId: Long? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: Date = Date(),

    @ColumnInfo(name = "price")
    var price: String = ""
) {
    @Ignore
    var createdAtAsString: String = DateFormat.getDateInstance( DateFormat.MEDIUM ).format( createdAt )
}
