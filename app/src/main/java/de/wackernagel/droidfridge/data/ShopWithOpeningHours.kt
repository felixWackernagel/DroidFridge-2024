package de.wackernagel.droidfridge.data

import androidx.room.Embedded
import androidx.room.Relation

data class ShopWithOpeningHours(
    @Embedded val shop: Shop,
    @Relation(
        parentColumn = "id",
        entityColumn = "shop_id"
    )
    val openingHours: List<OpeningHours>
)

fun ShopWithOpeningHours.isOpen(): Boolean = false
fun ShopWithOpeningHours.isClosedSoon(): Boolean = false