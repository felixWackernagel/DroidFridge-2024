package de.wackernagel.droidfridge.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
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
): ViewModel() {
    sealed class UiEvent {
        data class MarkedAsFavorite(val shopName: String): UiEvent()
        data class UnmarkedAsFavorite(val shopName: String): UiEvent()
        data class ShopDeleted(val shopName: String): UiEvent()
        data class NavigateToEditShop(val shopId: Long): UiEvent()
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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
                _uiEvent.emit( UiEvent.MarkedAsFavorite( shopName = it.name ) )
            } else {
                _uiEvent.emit( UiEvent.UnmarkedAsFavorite( shopName = it.name ) )
            }
        }
    }

    fun deleteShop() = viewModelScope.launch {
        shop.value?.shop?.let {
            shopRepository.delete( it )
            _uiEvent.emit( UiEvent.ShopDeleted( shopName = it.name ) )
        }
    }

    fun navigateToEditor() {
        viewModelScope.launch {
            shop.value?.shop?.let {
                _uiEvent.emit(UiEvent.NavigateToEditShop( shopId = it.id ))
            }
        }
    }

    fun update(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopRepository.update(shop)
        }
    }
}
