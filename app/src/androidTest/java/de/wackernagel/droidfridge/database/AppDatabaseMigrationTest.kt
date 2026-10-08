package de.wackernagel.droidfridge.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import de.wackernagel.droidfridge.di.MIGRATION_1_2
import de.wackernagel.droidfridge.di.MIGRATION_2_3
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

private const val TEST_DB = "migration-test.db"

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        val dbV1 = helper.createDatabase(TEST_DB, 1)
        dbV1.execSQL("INSERT INTO shops (id, name, is_favorite) VALUES (1, 'Supermarket V1', 1)")
        dbV1.execSQL("INSERT INTO opening_hours (id, shop_id, start, end, day) VALUES (10, 1, '08:00', '20:00', 1)")
        dbV1.close()

        val dbV2 = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        val shopCursor = dbV2.query("SELECT * FROM shops WHERE id = 1")
        assertTrue(shopCursor.moveToFirst())
        assertEquals("Supermarket V1", shopCursor.getString(shopCursor.getColumnIndexOrThrow("name")))
        assertEquals(1, shopCursor.getInt(shopCursor.getColumnIndexOrThrow("is_favorite")))
        shopCursor.close()

        val hoursCursor = dbV2.query("SELECT * FROM opening_hours WHERE id = 10")
        assertTrue(hoursCursor.moveToFirst())
        assertEquals("08:00", hoursCursor.getString(hoursCursor.getColumnIndexOrThrow("start")))
        assertEquals("20:00", hoursCursor.getString(hoursCursor.getColumnIndexOrThrow("end")))
        hoursCursor.close()

        dbV2.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        val dbV2 = helper.createDatabase(TEST_DB, 2)
        dbV2.execSQL("INSERT INTO shops (id, name, city) VALUES (2, 'Bakery V2', 'Berlin')")
        dbV2.execSQL("INSERT INTO opening_hours (id, shop_id, start, end, day) VALUES (20, 2, '06:00', '18:00', 2)")
        dbV2.close()

        val dbV3 = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)

        val shopCursor = dbV3.query("SELECT * FROM shops WHERE id = 2")
        assertTrue(shopCursor.moveToFirst())
        assertEquals("Bakery V2", shopCursor.getString(shopCursor.getColumnIndexOrThrow("name")))
        assertEquals("Berlin", shopCursor.getString(shopCursor.getColumnIndexOrThrow("city")))
        shopCursor.close()

        val hoursCursor = dbV3.query("SELECT * FROM opening_hours WHERE id = 20")
        assertTrue(hoursCursor.moveToFirst())
        assertEquals(2, hoursCursor.getInt(hoursCursor.getColumnIndexOrThrow("shop_id")))
        assertEquals(2, hoursCursor.getInt(hoursCursor.getColumnIndexOrThrow("day")))
        hoursCursor.close()

        val indexCursor = dbV3.query("SELECT name FROM sqlite_master WHERE type = 'index' AND name = 'index_opening_hours_shop_id_day'")
        assertTrue(indexCursor.moveToFirst())
        assertEquals("index_opening_hours_shop_id_day", indexCursor.getString(0))
        indexCursor.close()

        dbV3.close()
    }

    @Test
    @Throws(IOException::class)
    fun migrateAllVersions_1To3() {
        val dbV1 = helper.createDatabase(TEST_DB, 1)
        dbV1.execSQL("INSERT INTO shops (id, name, category) VALUES (3, 'Full Migration Shop', 'Grocery')")
        dbV1.execSQL("INSERT INTO opening_hours (id, shop_id, start, end, day) VALUES (30, 3, '09:00', '19:00', 3)")
        dbV1.close()

        val dbV3 = helper.runMigrationsAndValidate(
            TEST_DB,
            3,
            true,
            MIGRATION_1_2,
            MIGRATION_2_3
        )

        val shopCursor = dbV3.query("SELECT * FROM shops WHERE id = 3")
        assertTrue(shopCursor.moveToFirst())
        assertEquals("Full Migration Shop", shopCursor.getString(shopCursor.getColumnIndexOrThrow("name")))
        assertEquals("Grocery", shopCursor.getString(shopCursor.getColumnIndexOrThrow("category")))
        shopCursor.close()

        val hoursCursor = dbV3.query("SELECT * FROM opening_hours WHERE id = 30")
        assertTrue(hoursCursor.moveToFirst())
        assertEquals("09:00", hoursCursor.getString(hoursCursor.getColumnIndexOrThrow("start")))
        assertEquals("19:00", hoursCursor.getString(hoursCursor.getColumnIndexOrThrow("end")))
        assertEquals(3, hoursCursor.getInt(hoursCursor.getColumnIndexOrThrow("day")))
        hoursCursor.close()

        dbV3.close()
    }

    @Test
    @Throws(IOException::class)
    fun roomDatabase_opensMigratedDatabase_successfully() {
        val dbV1 = helper.createDatabase(TEST_DB, 1)
        dbV1.execSQL("INSERT INTO shops (id, name) VALUES (4, 'Room Validation Shop')")
        dbV1.close()

        // Validate that Room can open the migrated database without schema mismatch errors
        val roomDb = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()

        val cursor = roomDb.query("SELECT * FROM shops WHERE id = 4", null)
        assertNotNull(cursor)
        assertTrue(cursor.moveToFirst())
        assertEquals("Room Validation Shop", cursor.getString(cursor.getColumnIndexOrThrow("name")))
        cursor.close()

        roomDb.close()
    }
}
