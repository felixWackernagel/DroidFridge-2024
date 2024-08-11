package com.example.myapplication.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Product
import com.example.myapplication.database.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val productRepository: ProductRepository) : BaseViewModel()  {
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
}