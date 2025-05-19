package de.wackernagel.droidfridge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import de.wackernagel.droidfridge.database.ShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    private val shopRepository: ShopRepository
): BaseViewModel() {

    data class UiState(
        val isLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow( UiState() )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val shops : LiveData<List<ShopWithOpeningHours>> = shopRepository.allShopsWithOpeningHours

    fun exampleStateChange() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true,
            )
        }
    }

//    fun delete(shop: Shop) = viewModelScope.launch {
//        withContext( Dispatchers.IO ) {
//            shopRepository.delete(shop)
//        }
//    }
//
//    fun update(shop: Shop) = viewModelScope.launch {
//        withContext( Dispatchers.IO ) {
//            shopRepository.update(shop)
//        }
//    }
}
