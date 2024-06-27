package com.example.myapplication.database

import android.content.Context
import com.example.myapplication.data.Product
import com.example.myapplication.data.ProductPrice
import com.example.myapplication.data.Shop
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DatabaseInitialization(val context: Context, val db: AppDatabase): Runnable {
    override fun run() {
        GlobalScope.launch {
            val product = Product()
            product.name = "Erdbeeren 250g"
            product.alias = "Erdbeeren"
            val productId = db.productDao.insert(product)

            val lidlShop = Shop()
            lidlShop.name = "Lidl"
            lidlShop.address = "Bautzner Landstraße 112, 01324 Dresden"
            lidlShop.openingHours = "Mo-Sa: 07:00 - 21:00"
            lidlShop.phone = " 0800 4353361"
            db.shopDao.insert(lidlShop)

            val price = ProductPrice()
            price.price = "2,99"
            price.productId = productId
            db.productPriceDao.insert(price)
        }
    }
}