package de.wackernagel.droidfridge.di

import dagger.assisted.AssistedFactory
import de.wackernagel.droidfridge.viewmodel.ShopViewModel

@AssistedFactory
interface ShopViewModelFactory {
    fun create( shopId: Long ): ShopViewModel
}