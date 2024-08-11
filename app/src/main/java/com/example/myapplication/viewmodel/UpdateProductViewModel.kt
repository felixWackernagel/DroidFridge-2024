package com.example.myapplication.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.ProductRepository
import com.example.myapplication.di.UpdateProductViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UpdateProductViewModelFactory::class)
class UpdateProductViewModel @AssistedInject constructor(
    @Assisted productId: Long,
    private val productRepository: ProductRepository
) : BaseViewModel() {
    var product = productRepository.get(productId)

    fun updateProduct() {
        _doValidation.value = true
    }

    override fun updateEntryAfterValidation() {
        viewModelScope.launch {
            try {
                productRepository.update(product.value!!)
                _navigateToList.value = true
            } catch(sqlExc: SQLiteConstraintException) {
                _insertSuccess.value = false
            }
        }
    }
}