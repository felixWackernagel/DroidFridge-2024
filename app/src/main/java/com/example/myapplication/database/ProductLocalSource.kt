package com.example.myapplication.database

import androidx.lifecycle.LiveData
import com.example.myapplication.dao.ProductDao
import com.example.myapplication.data.Product
import kotlinx.coroutines.flow.Flow
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