package de.wackernagel.droidfridge.database

import androidx.lifecycle.LiveData
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import javax.inject.Inject

class ShopRepository @Inject constructor( private val shopSource: ShopLocalSource ) {

    val allShopsWithOpeningHours: LiveData<List<ShopWithOpeningHours>> = shopSource.getShopsWithOpeningHours()

    fun getShopWithOpeningHours(shopId: Long): LiveData<ShopWithOpeningHours> {
        return shopSource.getShopWithOpeningHours(shopId)
    }

    fun get(shopId: Long): LiveData<Shop> {
        return shopSource.get(shopId)
    }

    suspend fun insert(shop: Shop){
        shopSource.insert(shop)
    }

    suspend fun delete(shop: Shop){
        shopSource.delete(shop)
    }

    suspend fun update(shop: Shop){
        shopSource.update(shop)
    }
}