package com.example.myapplication.di

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.MyApplication
import com.example.myapplication.dao.ProductDao
import com.example.myapplication.dao.ProductPriceDao
import com.example.myapplication.dao.ShopDao
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.DatabaseInitialization
import com.example.myapplication.database.ProductLocalSource
import com.example.myapplication.database.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
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