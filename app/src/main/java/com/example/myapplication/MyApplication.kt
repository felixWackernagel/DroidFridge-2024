package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.ProductLocalSource
import com.example.myapplication.database.ProductRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class MyApplication: Application() {

    companion object {
        val TAG = this::class.simpleName;

        var appCtx: Context? = null

        private const val APP_PREFIX = "my_application_"
        const val APP_FILEPATH_PREFIX = APP_PREFIX + "files_"
        const val APP_DB_NAME = APP_PREFIX + "database"
    }
}