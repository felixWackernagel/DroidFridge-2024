package de.wackernagel.droidfridge

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

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