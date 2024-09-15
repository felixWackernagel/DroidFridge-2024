package de.wackernagel.droidfridge.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import de.wackernagel.droidfridge.data.Product
import de.wackernagel.droidfridge.database.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val productRepository: ProductRepository) : BaseViewModel()  {
    var newProduct = Product()

    fun addProduct() {
        _doValidation.value = true
    }

    override fun updateEntryAfterValidation() {
        viewModelScope.launch {
            try {
                productRepository.insert( newProduct )
                listItems()
            } catch(sqlExc: SQLiteConstraintException) {
                _insertSuccess.value = false
            }
        }
    }
}