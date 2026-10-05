package de.wackernagel.droidfridge.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import de.wackernagel.droidfridge.worker.AppUpdateWorker

class AppUpdateReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "DroidFridge"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if( intent.action != Intent.ACTION_MY_PACKAGE_REPLACED )
            return

        Log.i(TAG, "Received app update > try to start worker")

        val request =
            OneTimeWorkRequestBuilder<AppUpdateWorker>()
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "app_update",
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

}