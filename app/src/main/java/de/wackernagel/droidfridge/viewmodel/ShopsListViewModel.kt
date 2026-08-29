package de.wackernagel.droidfridge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import de.wackernagel.droidfridge.database.ShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    private val shopRepository: ShopRepository
): ViewModel() {

    sealed class UiEvent {
        data class NavigateToShop(val shopId: Long): UiEvent()
        data object NavigateToAddShop: UiEvent()
    }

    data class UiState(
        val isLoading: Boolean = false
    )

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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

    sealed class ShopsListAction {
        data class DeleteShop( val shop: Shop ): ShopsListAction()
        data class UpdateShop( val shop: Shop ): ShopsListAction()

        data object NavigateToAddShop: ShopsListAction()

        data class NavigateToShop( val shopId: Long ): ShopsListAction()
    }

    fun onAction( action: ShopsListAction) {
        when( action ) {
            is ShopsListAction.DeleteShop -> delete(action.shop)
            is ShopsListAction.UpdateShop -> update(action.shop)
            is ShopsListAction.NavigateToAddShop -> navigateToAddShop()
            is ShopsListAction.NavigateToShop -> navigateToShop(action.shopId)
        }
    }

    private fun delete(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopRepository.delete(shop)
        }
    }

    private fun update(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopRepository.update(shop)
        }
    }

    private fun navigateToAddShop() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToAddShop)
        }
    }

    private fun navigateToShop( shopId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.NavigateToShop( shopId = shopId ))
        }
    }
}
