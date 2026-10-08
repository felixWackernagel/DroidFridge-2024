package de.wackernagel.droidfridge.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class ShopEntityTest {

    @Test
    fun formattedAddress_joinsAddressPartsCorrectly() {
        val shop = Shop(
            name = "Test Shop",
            street = "Main Street",
            streetNumber = "42",
            postalCode = "12345",
            city = "Cityville",
            country = "Germany"
        )

        val expected = "Main Street 42\n12345 Cityville\nGermany"
        assertEquals(expected, shop.formattedAddress)
    }

    @Test
    fun hasCoordinates_returnsTrueWhenBothPresent() {
        val shopWithCoords = Shop(latitude = 50.0, longitude = 10.0)
        val shopWithoutCoords = Shop(latitude = 50.0, longitude = null)

        assertTrue(shopWithCoords.hasCoordinates)
        assertFalse(shopWithoutCoords.hasCoordinates)
    }

    @Test
    fun shopWithOpeningHours_isOpenAndClosedSoon() {
        val shop = Shop(name = "Test Supermarket")
        // Monday (day = 1) from 08:00 to 20:00
        val openingHours = listOf(
            OpeningHours(id = 1L, shopId = 1L, start = "08:00", end = "14:00", day = 1),
            OpeningHours(id = 2L, shopId = 1L, start = "15:00", end = "20:00", day = 1)
        )
        val shopWithHours = ShopWithOpeningHours(shop, openingHours)

        // Monday 10:00 AM -> Open, not closing soon
        val mondayMorning = LocalDateTime.of(2025, 3, 3, 10, 0) // 2025-03-03 is a Monday
        assertTrue(shopWithHours.isOpen(mondayMorning))
        assertFalse(shopWithHours.isClosedSoon(mondayMorning))

        // Monday 14:30 -> Closed
        val mondayLunch = LocalDateTime.of(2025, 3, 3, 14, 30) // 2025-03-03 is a Monday
        assertFalse(shopWithHours.isOpen(mondayLunch))
        assertFalse(shopWithHours.isClosedSoon(mondayLunch))

        // Monday 19:30 -> Open and closing soon (within 1 hour of 20:00)
        val mondayEvening = LocalDateTime.of(2025, 3, 3, 19, 30)
        assertTrue(shopWithHours.isOpen(mondayEvening))
        assertTrue(shopWithHours.isClosedSoon(mondayEvening))

        // Monday 20:30 -> Closed
        val mondayNight = LocalDateTime.of(2025, 3, 3, 20, 30)
        assertFalse(shopWithHours.isOpen(mondayNight))
        assertFalse(shopWithHours.isClosedSoon(mondayNight))
    }
}
