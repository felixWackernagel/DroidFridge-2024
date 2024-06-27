package com.example.myapplication.viewmodel

import com.example.myapplication.database.ProductRepository
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UpdateProductViewModel (productId: Long, private val productRepository: ProductRepository) : BaseViewModel() {
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

    @Suppress("UNCHECKED_CAST")
    class Factory(private val productId: Long, private val productRepository: ProductRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UpdateProductViewModel::class.java)) {
                return UpdateProductViewModel(productId, productRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}