package de.wackernagel.droidfridge.di

import dagger.assisted.AssistedFactory
import de.wackernagel.droidfridge.viewmodel.UpdateShopViewModel

@AssistedFactory
interface UpdateShopViewModelFactory {
    fun create(shopId: Long ): UpdateShopViewModel
}