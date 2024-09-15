package de.wackernagel.droidfridge.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.database.ShopRepository
import de.wackernagel.droidfridge.di.UpdateShopViewModelFactory
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UpdateShopViewModelFactory::class)
class UpdateShopViewModel @AssistedInject constructor(
    @Assisted shopId: Long,
    private val shopRepository: ShopRepository
) : BaseViewModel() {
    var shop = shopRepository.get( shopId )

    fun updateShop() {
        _doValidation.value = true
    }

    override fun updateEntryAfterValidation() {
        viewModelScope.launch {
            try {
                shopRepository.update( shop.value!! )
                listItems()
            } catch(sqlExc: SQLiteConstraintException) {
                _insertSuccess.value = false
            }
        }
    }
}