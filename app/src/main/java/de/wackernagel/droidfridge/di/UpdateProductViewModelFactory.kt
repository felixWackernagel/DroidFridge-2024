package de.wackernagel.droidfridge.di

import de.wackernagel.droidfridge.viewmodel.UpdateProductViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UpdateProductViewModelFactory {
    fun create( productId: Long ): UpdateProductViewModel
}