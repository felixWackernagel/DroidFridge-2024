package de.wackernagel.droidfridge.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

class Helpers {
    companion object {
        fun startIntentWhenAvailable(context: Context, intent: Intent) {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
                if( context.packageManager.queryIntentActivities( intent, PackageManager.ResolveInfoFlags.of( PackageManager.MATCH_DEFAULT_ONLY.toLong() ) ).isNotEmpty() ) {
                    context.startActivity(intent)
                }
            }
            else {
                if( context.packageManager.queryIntentActivities( intent, PackageManager.MATCH_DEFAULT_ONLY ).isNotEmpty() ) {
                    context.startActivity(intent)
                }
            }
        }
    }
}