package de.wackernagel.droidfridge.fragment

import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.ui.formfields.FormFieldText
import de.wackernagel.droidfridge.ui.formfields.validate
import de.wackernagel.droidfridge.viewmodel.BaseViewModel
import kotlinx.coroutines.launch


abstract class BaseFragment: Fragment() {

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

    fun activateFavoriteFAB( viewModelFunc: () -> Unit, isFavorite: Boolean ) {
        val fab = this.requireActivity().findViewById<FloatingActionButton>(R.id.favorite_floating_action_button)
        fab.setImageResource( if( isFavorite ) R.drawable.ic_favorite_filled_24 else R.drawable.ic_favorite_outlined_24 )
        fab.show()
        fab.setOnClickListener { viewModelFunc() }

        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior?
        if( behavior != null ) {
            behavior.isAutoHideEnabled = true
        }
    }

    fun deactivateFavoriteFAB() {
        val fab = this.requireActivity().findViewById<FloatingActionButton>(R.id.favorite_floating_action_button)
        if( fab.isVisible ) {
            // floating action button can't be hidden because of app:layout_anchor attribute
            // that is the reason why we set autoHideEnabled to false
            val params = fab.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior as FloatingActionButton.Behavior?
            if( behavior != null ) {
                behavior.isAutoHideEnabled = false
            }

            fab.hide()
        }
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