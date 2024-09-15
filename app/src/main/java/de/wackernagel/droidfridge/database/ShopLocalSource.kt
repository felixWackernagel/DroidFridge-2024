package de.wackernagel.droidfridge.database

import androidx.lifecycle.LiveData
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import javax.inject.Inject

class ShopLocalSource @Inject constructor(private val shopDao: ShopDao) {

    fun getShopsWithOpeningHours(): LiveData<List<ShopWithOpeningHours>> {
        return shopDao.getShopsWithOpeningHours()
    }

    fun getShopWithOpeningHours( shopId: Long): LiveData<ShopWithOpeningHours> {
        return shopDao.getShopWithOpeningHours(shopId)
    }

    fun get(shopId: Long): LiveData<Shop> {
        return shopDao.get(shopId)
    }

    suspend fun insert( shop: Shop) {
        shopDao.insert( shop )
    }

    suspend fun update( shop: Shop ) {
        shopDao.update( shop )
    }

    suspend fun delete( shop: Shop) {
        shopDao.delete(shop)
    }
}