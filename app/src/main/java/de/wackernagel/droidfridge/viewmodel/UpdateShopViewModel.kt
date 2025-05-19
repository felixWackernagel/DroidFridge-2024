package de.wackernagel.droidfridge.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.database.ShopRepository
import de.wackernagel.droidfridge.di.UpdateShopViewModelFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UpdateShopViewModelFactory::class)
class UpdateShopViewModel @AssistedInject constructor(
    @Assisted shopId: Long,
    private val shopRepository: ShopRepository
) : BaseViewModel() {
    sealed class UiEvent {
        data class ShopUpdated(val shopName: String): UiEvent()
        data class ShopDeleted(val shopName: String): UiEvent()
        data object ShopUpdateError: UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var shop = shopRepository.get( shopId )

    fun updateShop() {
        _doValidation.value = true
    }

    override fun updateEntryAfterValidation() {
        viewModelScope.launch {
            try {
                shopRepository.update( shop.value!! )
                _eventFlow.emit( UiEvent.ShopUpdated( shopName = shop.value!!.name ) )
                showItemDetails(shop.value!!.id)
            } catch(sqlExc: SQLiteConstraintException) {
                _eventFlow.emit( UiEvent.ShopUpdateError )
            }
        }
    }

    fun deleteShop() = viewModelScope.launch {
        shop.value?.let {
            shopRepository.delete( it )
            _eventFlow.emit( UiEvent.ShopDeleted( shopName = it.name ) )
            listItems()
        }
    }
}