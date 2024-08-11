package de.wackernagel.droidfridge.database

import androidx.lifecycle.LiveData
import de.wackernagel.droidfridge.dao.ProductDao
import de.wackernagel.droidfridge.data.Product
import javax.inject.Inject

class ProductLocalSource @Inject constructor( private val productDao: ProductDao) {

    fun getAllProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    fun get(productId: Long): LiveData<Product> {
        return productDao.get(productId)
    }

    suspend fun insert( product: Product) {
        productDao.insert( product )
    }

    suspend fun update( product: Product ) {
        productDao.update( product )
    }

    suspend fun delete( product: Product) {
        productDao.delete(product)
    }
}