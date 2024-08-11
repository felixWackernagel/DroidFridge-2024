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
    foreignKeys = [ForeignKey(entity = Product::class, childColumns = ["product_id"], parentColumns = ["id"])],
    indices = [Index(value=["id", "product_id"])]
)
data class ProductPrice(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,
    @ColumnInfo(name = "product_id")
    var productId: Long? = null,
    var creationDate: Date = Date(),
    var price: String = ""
) {
    @Ignore
    var creationDateAsString: String = DateFormat.getDateInstance( DateFormat.MEDIUM ).format( creationDate )
}
