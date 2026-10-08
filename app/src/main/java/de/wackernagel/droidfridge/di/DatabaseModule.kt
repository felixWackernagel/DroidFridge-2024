package de.wackernagel.droidfridge.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.wackernagel.droidfridge.Preferences
import de.wackernagel.droidfridge.dao.OpeningHoursDao
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.database.AppDatabase
import de.wackernagel.droidfridge.database.ShopLocalSource
import de.wackernagel.droidfridge.database.ShopRepository
import javax.inject.Singleton

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Version 1 and Version 2 schemas are identical; no database structural changes required.
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP INDEX IF EXISTS `index_opening_hours_shop_id`")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_opening_hours_shop_id_day` ON `opening_hours` (`shop_id`, `day`)")
    }
}

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun providePreferences( app: Application ): Preferences {
        return Preferences(app)
    }

    @Provides
    @Singleton
    fun provideAppDatabase( app: Application ): AppDatabase {
        return Room.databaseBuilder( app, AppDatabase::class.java, "droidfridge.db" )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun provideShopDao( db: AppDatabase ): ShopDao {
        return db.shopDao
    }

    @Provides
    @Singleton
    fun provideOpeningHoursDao( db: AppDatabase ): OpeningHoursDao {
        return db.openingHours
    }

    @Provides
    @Singleton
    fun provideShopLocalSource( shopDao: ShopDao ): ShopLocalSource {
        return ShopLocalSource( shopDao )
    }

    @Provides
    @Singleton
    fun provideShopRepository( shopLocalSource: ShopLocalSource ): ShopRepository {
        return ShopRepository( shopLocalSource )
    }
}