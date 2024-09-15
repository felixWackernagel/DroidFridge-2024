package de.wackernagel.droidfridge.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.database.ShopRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddShopViewModel @Inject constructor(private val shopRepository: ShopRepository) : BaseViewModel()  {
    sealed class UiEvent {
        data class ShopCreated(val shopName: String): UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var newShop = Shop()

    fun addShop() {
        _doValidation.value = true
    }

    override fun updateEntryAfterValidation() {
        viewModelScope.launch {
            try {
                shopRepository.insert( newShop )
                _eventFlow.emit( UiEvent.ShopCreated( shopName = newShop.name ) )
                listItems()
            } catch(sqlExc: SQLiteConstraintException) {
                _insertSuccess.value = false
            }
        }
    }
}