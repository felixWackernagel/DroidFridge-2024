package de.wackernagel.droidfridge.data

import androidx.room.Embedded
import androidx.room.Relation
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

data class ShopWithOpeningHours(
    @Embedded val shop: Shop,
    @Relation(
        entity = OpeningHours::class,
        parentColumn = "id",
        entityColumn = "shop_id"
    )
    val openingHours: List<OpeningHours>
)

/**
 * Checks if the shop is currently open at the given time (defaults to [LocalDateTime.now]).
 */
fun ShopWithOpeningHours.isOpen(now: LocalDateTime = LocalDateTime.now()): Boolean {
    val todayHours = getTodayOpeningHours(now)
    if( todayHours.isEmpty() ) return false
    return todayHours.any { isTimeWithinRange(now.toLocalTime(), it.start, it.end) }
}

/**
 * Checks if the shop is closing within the next hour from the given time (defaults to [LocalDateTime.now]).
 */
fun ShopWithOpeningHours.isClosedSoon(now: LocalDateTime = LocalDateTime.now()): Boolean {
    val todayHours = getTodayOpeningHours(now)
    if( todayHours.isEmpty() ) return false
    val currentTime = now.toLocalTime()

    val currentOpeningHours = todayHours.findLast { isTimeWithinRange(currentTime, it.start, it.end) }
    if ( currentOpeningHours == null ) return false

    return try {
        val endTime = LocalTime.parse(currentOpeningHours.end)
        val oneHourBeforeEnd = endTime.minusHours(1)
        currentTime in oneHourBeforeEnd..<endTime
    } catch (_: DateTimeParseException) {
        false
    }
}

private fun ShopWithOpeningHours.getTodayOpeningHours(now: LocalDateTime): List<OpeningHours> {
    val currentDay = now.dayOfWeek.value // 1 (Monday) to 7 (Sunday)
    return openingHours.filter { it.day == currentDay }
}

private fun isTimeWithinRange(currentTime: LocalTime, start: String, end: String): Boolean {
    return try {
        val startTime = LocalTime.parse(start)
        val endTime = LocalTime.parse(end)
        currentTime in startTime..<endTime
    } catch (_: DateTimeParseException) {
        false
    }
}
