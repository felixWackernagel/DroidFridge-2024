package de.wackernagel.droidfridge.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.database.ShopRepository
import de.wackernagel.droidfridge.di.ShopViewModelFactory
import de.wackernagel.droidfridge.ui.Helpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.StringJoiner

@HiltViewModel(assistedFactory = ShopViewModelFactory::class)
class ShopViewModel @AssistedInject constructor(
    @Assisted shopId: Long,
    private val shopRepository: ShopRepository
): BaseViewModel() {
    sealed class UiEvent {
        data class MarkedAsFavorite(val shopName: String): UiEvent()
        data class UnmarkedAsFavorite(val shopName: String): UiEvent()
        data class ShopDeleted(val shopName: String): UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val shop = shopRepository.getShopWithOpeningHours( shopId )

    fun openMap(context: Context, shop: Shop ) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            val joiner = StringJoiner(" ")
            listOf( shop.street, shop.streetNumber, shop.postalCode, shop.city, shop.country )
                .filter { !it.isNullOrEmpty() }
                .forEach( joiner::add )
            data = "geo:0,0?q=${joiner}".toUri()
        }
        Helpers.startIntentWhenAvailable( context, intent )
    }

    fun dialPhoneNumber( context: Context, shop: Shop ) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:${shop.phone}".toUri()
        }
        Helpers.startIntentWhenAvailable( context, intent )
    }

    fun toggleFavoriteShop() = viewModelScope.launch {
        shop.value?.shop?.let{
            it.isFavorite = !it.isFavorite
            update( it )
            if( it.isFavorite ) {
                _eventFlow.emit( UiEvent.MarkedAsFavorite( shopName = it.name ) )
            } else {
                _eventFlow.emit( UiEvent.UnmarkedAsFavorite( shopName = it.name ) )
            }
        }
    }

    fun deleteShop() = viewModelScope.launch {
        shop.value?.shop?.let {
            shopRepository.delete( it )
            _eventFlow.emit( UiEvent.ShopDeleted( shopName = it.name ) )
            listItems()
        }
    }

    fun navigateToEditor() {
        shop.value?.shop?.let {
            editItem( it.id )
        }
    }

    fun update(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopRepository.update(shop)
        }
    }
}
