package com.example.myapplication.ui.formfields

import android.util.Log
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import reactivecircus.flowbinding.android.widget.textChanges

class FormFieldText(
    scope: CoroutineScope,
    private val textInputLayout: TextInputLayout,
    private val textInputEditText: TextInputEditText,
    private val validation: suspend (String?) -> String? = { null }
) : FormField<String>() {
    var isEnabled: Boolean
        get() = textInputLayout.isEnabled
        set(value) {
            textInputLayout.isEnabled = value
        }
    var isVisible: Boolean
        get() = textInputLayout.isVisible
        set(value) {
            textInputLayout.isVisible = value
        }
    var value: String?
        get() = _state.value
        set(value) {
            textInputEditText.setText(value)
        }
    init {
        textInputEditText.textChanges().skipInitialValue().onEach { text ->
            clearError()
            _state.update { text.toString() }
            Log.d("FormFieldText","Text of textInputEditText: ${text.toString()}")
        }.launchIn(scope)
    }
    override fun clearError() {
        if (textInputLayout.error != null) {
            textInputLayout.error = null
            textInputLayout.isErrorEnabled = false
        }
    }
    override fun clearFocus() {
        textInputEditText.clearFocus()
    }
    override fun disable() {
        isEnabled = false
    }
    override fun enable() {
        isEnabled = true
    }
    override suspend fun validate(focusIfError: Boolean): Boolean {
        if (!isVisible) {
            return true
        }
        val errorValue = try {
            validation(_state.value)
        } catch (error: Throwable) {
            error.message
        }
        val result = errorValue == null
        if (result) {
            clearError()
        } else {
            textInputLayout.error = errorValue
            if (focusIfError) {
                textInputEditText.requestFocus()
            }
        }
        _isValid.update { result }
        return result
    }
}