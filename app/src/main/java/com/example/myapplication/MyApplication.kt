package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.ProductLocalSource
import com.example.myapplication.database.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getInstance( this ) }
    private val productSource by lazy { ProductLocalSource( database.productDao ) }
    val productRepository by lazy { ProductRepository( productSource ) }
    val shopDao by lazy { database.shopDao }

    companion object {
        val TAG = this::class.simpleName;

        var appCtx: Context? = null

        private const val APP_PREFIX = "my_application_"
        const val APP_FILEPATH_PREFIX = APP_PREFIX + "files_"
        const val APP_DB_NAME = APP_PREFIX + "database"
    }
}