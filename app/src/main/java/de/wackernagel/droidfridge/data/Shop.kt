package de.wackernagel.droidfridge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "shops" )
data class Shop(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "street")
    var street: String? = null,
    @ColumnInfo(name = "street_number")
    var streetNumber: String? = null,
    @ColumnInfo(name = "postal_code")
    var postalCode: String? = null,
    @ColumnInfo(name = "city")
    var city: String? = null,
    @ColumnInfo(name = "country")
    var country: String? = null,
    @ColumnInfo(name = "latitude")
    var latitude: Double? = null,
    @ColumnInfo(name = "longitude")
    var longitude: Double? = null,
    @ColumnInfo(name = "phone")
    var phone: String? = null,
    @ColumnInfo(name = "image_path")
    var imagePath: String? = null,
    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "details")
    var details: String? = null,
    @ColumnInfo(name = "category")
    var category: String? = null
) {
    /**
     * Returns a formatted multi-line address string if address details exist.
     */
    val formattedAddress: String
        get() {
            val streetLine = listOfNotNull(street?.takeIf { it.isNotBlank() }, streetNumber?.takeIf { it.isNotBlank() })
                .joinToString(" ")
            val cityLine = listOfNotNull(postalCode?.takeIf { it.isNotBlank() }, city?.takeIf { it.isNotBlank() })
                .joinToString(" ")
            val countryLine = country?.takeIf { it.isNotBlank() }
            return listOfNotNull(
                streetLine.takeIf { it.isNotBlank() },
                cityLine.takeIf { it.isNotBlank() },
                countryLine
            ).joinToString("\n")
        }

    /**
     * Checks if coordinates (latitude & longitude) are set for this shop.
     */
    val hasCoordinates: Boolean
        get() = latitude != null && longitude != null
}
