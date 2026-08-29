package de.wackernagel.droidfridge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.Preferences
import de.wackernagel.droidfridge.usecase.CreateSampleDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: Preferences,
    private val createSampleDataUseCase: CreateSampleDataUseCase
): ViewModel() {

    val showSampleCreatorAction: Flow<Boolean> = preferences.showSampleCreator

    fun runSampler() = viewModelScope.launch {
        createSampleDataUseCase()
        preferences.setShowSampleCreator(false)
    }
}
