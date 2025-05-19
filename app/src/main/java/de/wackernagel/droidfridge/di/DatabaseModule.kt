package de.wackernagel.droidfridge.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.database.AppDatabase
import de.wackernagel.droidfridge.database.ShopLocalSource
import de.wackernagel.droidfridge.database.ShopRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase( app: Application ): AppDatabase {
        return Room.databaseBuilder( app, AppDatabase::class.java, "droidfridge.db" )
            .createFromAsset( "database/sampled_droidfridge.db" )
            .build()
    }

    @Provides
    @Singleton
    fun provideShopDao( db: AppDatabase ): ShopDao {
        return db.shopDao
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