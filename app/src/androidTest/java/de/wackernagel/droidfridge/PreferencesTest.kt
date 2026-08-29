package de.wackernagel.droidfridge

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PreferencesTest {

    private lateinit var context: Context
    private lateinit var preferencesManager: Preferences

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        preferencesManager = Preferences(context)
    }

    @Test
    fun showSampleCreator_isTrueByDefault_andFalseAfterSet() = runTest {
        val initialValue = preferencesManager.showSampleCreator.first()
        assertTrue(initialValue)

        preferencesManager.setShowSampleCreator(false)

        val updatedValue = preferencesManager.showSampleCreator.first()
        assertFalse(updatedValue)
    }
}