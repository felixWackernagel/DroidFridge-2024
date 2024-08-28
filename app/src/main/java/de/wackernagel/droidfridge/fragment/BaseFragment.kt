package de.wackernagel.droidfridge.fragment

import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.ui.formfields.FormFieldText
import de.wackernagel.droidfridge.ui.formfields.validate
import de.wackernagel.droidfridge.viewmodel.BaseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

abstract class BaseFragment: Fragment() {
    val TAG = this::class.simpleName

    fun saveToUpdateButton(saveButton: Button) {
        saveButton.text = resources.getString(R.string.update_button)
    }

    fun activateAddFAB(viewModelFunc: () -> Unit ) {
        val fab = this.requireActivity().findViewById<FloatingActionButton>(R.id.add_floating_action_button)
        fab.show()
        fab.setOnClickListener { viewModelFunc() }
    }

    fun deactivateAddFAB() {
        val fab = this.requireActivity().findViewById<FloatingActionButton>(R.id.add_floating_action_button)
        fab.hide()
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