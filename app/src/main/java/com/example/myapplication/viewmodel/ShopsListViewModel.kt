package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dao.ShopDao
import com.example.myapplication.data.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopsListViewModel(private val shopDao: ShopDao): BaseViewModel() {
    val shops : LiveData<List<Shop>> = shopDao.getAllShops()

    fun insert(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopDao.insert(shop)
        }
    }

    fun delete(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopDao.delete(shop)
        }
    }

    fun update(shop: Shop) = viewModelScope.launch {
        withContext( Dispatchers.IO ) {
            shopDao.update(shop)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val shopDao: ShopDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ShopsListViewModel::class.java)) {
                return ShopsListViewModel(shopDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}
