package de.wackernagel.droidfridge

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.common.truth.Truth.assertThat
import de.wackernagel.droidfridge.broadcastreceiver.AppUpdateReceiver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppUpdateReceiverTest {

    private lateinit var context: Context
    private lateinit var workManager: WorkManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        WorkManagerTestInitHelper.initializeTestWorkManager(context)

        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun intentAction_enqueuesAppUpdateWorker() {
        // Given
        val receiver = AppUpdateReceiver()

        // When
        receiver.onReceive(
            context,
            Intent(Intent.ACTION_MY_PACKAGE_REPLACED)
        )

        // Then
        val workInfos = workManager
            .getWorkInfosForUniqueWork("app_update")
            .get()

        assertThat(workInfos).hasSize(1)
    }

    @Test
    fun otherIntent_isIgnored() {
        // Given
        val receiver = AppUpdateReceiver()

        val intent = Intent(Intent.ACTION_BOOT_COMPLETED)

        // When
        receiver.onReceive(context, intent)

        // Then
        val workInfos = workManager
            .getWorkInfosForUniqueWork("app_update")
            .get()

        assertThat(workInfos).isEmpty()
    }
}