package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Product
import com.example.myapplication.database.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(private val repository: ProductRepository): BaseViewModel() {
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
}
