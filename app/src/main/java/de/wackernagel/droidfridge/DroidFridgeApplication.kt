package de.wackernagel.droidfridge

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DroidFridgeApplication: Application() {

    companion object {
        val TAG = this::class.simpleName;

        var appCtx: Context? = null
    }
}