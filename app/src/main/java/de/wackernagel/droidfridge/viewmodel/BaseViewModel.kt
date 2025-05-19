package de.wackernagel.droidfridge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    private val _navigateToList = MutableLiveData(false)
    val navigateToList: LiveData<Boolean> = _navigateToList

    private val _navigateToAddItem = MutableLiveData(false)
    val navigateToAddItem: LiveData<Boolean>  = _navigateToAddItem

    private val _navigateToEditItem= MutableLiveData<Long?>()
    val navigateToEditItem: LiveData<Long?> = _navigateToEditItem

    private val _navigateToDetailItem = MutableLiveData<Long?>()
    val navigateToDetailItem: LiveData<Long?> = _navigateToDetailItem

    protected val _doValidation = MutableLiveData(false)
    val doValidation: LiveData<Boolean> = _doValidation

    fun addItem() {
        _navigateToAddItem.value = true
    }

    fun editItem(itemId: Long) {
        _navigateToEditItem.value = itemId
    }

    fun listItems() {
        _navigateToList.value = true
    }

    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    fun showItemDetails(itemId: Long) {
        _navigateToDetailItem.value = itemId
    }

    fun onShowItemDetailsNavigated() {
        _navigateToDetailItem.value = null
    }

    fun onItemAddNavigated() {
        _navigateToAddItem.value = false
    }

    fun onItemEditNavigated() {
        _navigateToEditItem.value = null
    }

    fun onValidatedAndUpdated() {
        _doValidation.value = false
    }

    open fun updateEntryAfterValidation() {
        // to be overridden by all view models that do validation
    }
}