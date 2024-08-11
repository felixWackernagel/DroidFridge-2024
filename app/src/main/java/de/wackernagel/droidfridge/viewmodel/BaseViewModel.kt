package de.wackernagel.droidfridge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    val TAG = this::class.simpleName
    val _navigateToList = MutableLiveData(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList
    val _navigateToAddItem = MutableLiveData(false)
    val navigateToAddItem: LiveData<Boolean>
        get() = _navigateToAddItem
    val _navigateToEditItem= MutableLiveData<Long?>()
    val navigateToEditItem: LiveData<Long?>
        get() = _navigateToEditItem

    val _insertSuccess = MutableLiveData<Boolean?>(null)
    val insertSuccess: LiveData<Boolean?>
        get() = _insertSuccess
    protected val _doValidation = MutableLiveData<Boolean>(false)
    val doValidation: LiveData<Boolean>
        get() = _doValidation

    fun addItem() {
        _navigateToAddItem.value = true
    }

    fun onNavigatedToList() {
        _navigateToList.value = false
    }

    fun onItemClicked(itemId: Long) {
        _navigateToEditItem.value = itemId
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