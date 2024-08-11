package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dao.ShopDao
import com.example.myapplication.data.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(private val shopDao: ShopDao): BaseViewModel() {
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
}
