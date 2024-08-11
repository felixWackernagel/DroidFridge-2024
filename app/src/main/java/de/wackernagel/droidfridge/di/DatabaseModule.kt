package de.wackernagel.droidfridge.di

import android.app.Application
import androidx.room.Room
import de.wackernagel.droidfridge.MyApplication
import de.wackernagel.droidfridge.dao.ProductDao
import de.wackernagel.droidfridge.dao.ProductPriceDao
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.database.AppDatabase
import de.wackernagel.droidfridge.database.ProductLocalSource
import de.wackernagel.droidfridge.database.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase( app: Application ): AppDatabase {
        return Room.databaseBuilder( app, AppDatabase::class.java, MyApplication.APP_DB_NAME)
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    Executors.newSingleThreadExecutor().execute({ db.runInTransaction( DatabaseInitialization(app, db ) ) })
//                }
//            })
            .build()
    }

    @Provides
    @Singleton
    fun provideProductDao( db: AppDatabase ): ProductDao {
        return db.productDao
    }

    @Provides
    @Singleton
    fun provideProductPriceDao( db: AppDatabase ): ProductPriceDao {
        return db.productPriceDao
    }

    @Provides
    @Singleton
    fun provideShopDao( db: AppDatabase ): ShopDao {
        return db.shopDao
    }

    @Provides
    @Singleton
    fun provideProductLocalSource( productDao: ProductDao ): ProductLocalSource {
        return ProductLocalSource( productDao )
    }

    @Provides
    @Singleton
    fun provideProductRepository( productLocalSource: ProductLocalSource ): ProductRepository {
        return ProductRepository( productLocalSource )
    }
}