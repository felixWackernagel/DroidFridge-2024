package de.wackernagel.droidfridge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.data.Shop
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
