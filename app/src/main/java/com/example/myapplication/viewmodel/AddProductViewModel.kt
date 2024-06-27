package com.example.myapplication.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Product
import com.example.myapplication.database.ProductRepository
import kotlinx.coroutines.launch

class AddProductViewModel(private val productRepository: ProductRepository) : BaseViewModel()  {
    var newProduct = Product()

    fun addProduct() {
        _doValidation.value = true
    }

    override fun updateEntryAfterValidation() {
        viewModelScope.launch {
            try {
                productRepository.insert( newProduct )
                _navigateToList.value = true
            } catch(sqlExc: SQLiteConstraintException) {
                _insertSuccess.value = false
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddProductViewModel::class.java)) {
                return AddProductViewModel(productRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}