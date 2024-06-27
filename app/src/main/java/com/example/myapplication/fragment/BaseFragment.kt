package com.example.myapplication.fragment

import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.ui.formfields.FormFieldText
import com.example.myapplication.ui.formfields.validate
import com.example.myapplication.viewmodel.BaseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

abstract class BaseFragment: Fragment() {
    val TAG = this::class.simpleName

    fun saveToUpdateButton(saveButton: Button) {
        saveButton.text = resources.getString(R.string.update_button)
    }

    fun activateAddFAB(viewModelFunc: () -> Unit ) {
        val fab = this.requireActivity().findViewById<FloatingActionButton>(R.id.add_floating_action_button)
        fab.isVisible = true
        fab.setOnClickListener { viewModelFunc() }
    }

    fun deactivateAddFAB() {
        val fab = this.requireActivity().findViewById<FloatingActionButton>(R.id.add_floating_action_button)
        fab.isVisible = false
    }

    fun formValidation(actionButton: Button, formFields: List<FormFieldText>, viewModel: BaseViewModel) = lifecycleScope.launch {
        actionButton.isEnabled = false

        // formFields.disable()
        if (formFields.validate(validateAll = true)) {
            viewModel.updateEntryAfterValidation()
            viewModel.onValidatedAndUpdated()
        }
        // formFields.enable()
        actionButton.isEnabled = true
    }
}