package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.Validation
import de.wackernagel.droidfridge.databinding.FragmentAddShopBinding
import de.wackernagel.droidfridge.ui.formfields.FormFieldText
import de.wackernagel.droidfridge.ui.formfields.validate
import de.wackernagel.droidfridge.viewmodel.AddProductViewModel
import de.wackernagel.droidfridge.viewmodel.AddShopViewModel
import de.wackernagel.droidfridge.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddShopFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddShopBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddShopViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddShopBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val fieldName = FormFieldText(
            scope = lifecycleScope,
            textInputLayout = binding.nameField,
            textInputEditText = binding.nameFieldInput,
            Validation.nameValidation
        )
        val formFields = listOf(fieldName)

        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_addShopFragment_to_shopsListFragment)
                viewModel.onNavigatedToList()
            }
        })

        viewModel.doValidation.observe(viewLifecycleOwner, Observer { doValidate ->
            if (doValidate) {
                formValidation(binding.saveButton, formFields, viewModel)
            }
        })

        viewModel.insertSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if( isSuccess != null && !isSuccess ) {
                Snackbar.make(
                    binding.saveButton,
                    resources.getText(R.string.unique_product_violation),
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        })

        binding.streetNameFieldInput.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.addShop()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

        return binding.root
    }

    private fun formValidation(actionButton: Button, formFields: List<FormFieldText>, viewModel: BaseViewModel) = lifecycleScope.launch {
        actionButton.isEnabled = false

        // formFields.disable()
        if (formFields.validate(validateAll = true)) {
            viewModel.updateEntryAfterValidation()
            viewModel.onValidatedAndUpdated()
        }
        // formFields.enable()
        actionButton.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}