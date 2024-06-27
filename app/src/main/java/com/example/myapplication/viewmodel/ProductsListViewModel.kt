package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Product
import com.example.myapplication.database.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsListViewModel(private val repository: ProductRepository): BaseViewModel() {
    val products : LiveData<List<Product>> = repository.allProducts

    fun insert(product: Product) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            repository.insert(product)
        }
    }

    fun delete(product: Product) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            repository.delete(product)
        }
    }

    fun update(product: Product) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            repository.update(product)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ProductRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ProductsListViewModel::class.java)) {
                return ProductsListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}
