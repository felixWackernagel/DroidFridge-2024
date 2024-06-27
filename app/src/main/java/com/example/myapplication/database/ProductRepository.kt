package com.example.myapplication.database

import androidx.lifecycle.LiveData
import com.example.myapplication.data.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productSource: ProductLocalSource) {

    val allProducts: LiveData<List<Product>> = productSource.getAllProducts()

    fun get(productId: Long): LiveData<Product> {
        return productSource.get(productId)
    }

    suspend fun insert(product: Product){
        productSource.insert(product)
    }

    suspend fun delete(product: Product){
        productSource.delete(product)
    }

    suspend fun update(product: Product){
        productSource.update(product)
    }
}