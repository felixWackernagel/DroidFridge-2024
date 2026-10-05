package de.wackernagel.droidfridge.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import de.wackernagel.droidfridge.BuildConfig
import de.wackernagel.droidfridge.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class AppUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val preferences: Preferences
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "DroidFridge"
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val previousVersion = preferences.getLastVersionCode()
            val currentVersion = BuildConfig.VERSION_CODE

            Log.i(TAG, "app update worker is started (from: $previousVersion > to: $currentVersion)")

            if( previousVersion < currentVersion ) {
                Log.i(TAG, "app update is done")
                preferences.setLastVersionCode(currentVersion)
            }

            Result.success()
        }
    }

}