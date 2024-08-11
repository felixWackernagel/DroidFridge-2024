package com.example.myapplication.di

import com.example.myapplication.viewmodel.UpdateProductViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface UpdateProductViewModelFactory {
    fun create( productId: Long ): UpdateProductViewModel
}