package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.Validation
import de.wackernagel.droidfridge.databinding.FragmentAddShopBinding
import de.wackernagel.droidfridge.ui.Result
import de.wackernagel.droidfridge.ui.ShopValidator
import de.wackernagel.droidfridge.ui.formfields.FormFieldText
import de.wackernagel.droidfridge.ui.formfields.validate
import de.wackernagel.droidfridge.viewmodel.AddShopViewModel
import de.wackernagel.droidfridge.viewmodel.BaseViewModel
import de.wackernagel.droidfridge.viewmodel.ShopViewModel
import kotlinx.coroutines.flow.collectLatest
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
        val themedInflater =  inflater.cloneInContext( ContextThemeWrapper( requireActivity(), R.style.Theme_DroidFridge ) )
        _binding = FragmentAddShopBinding.inflate(themedInflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToList.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_addShopFragment_to_shopsListFragment)
                viewModel.onNavigatedToList()
            }
        }

        viewModel.doValidation.observe(viewLifecycleOwner) { doValidate ->
            if (doValidate) {
                lifecycleScope.launch {
                    binding.saveButton.isEnabled = false
                    when (val result =
                        ShopValidator().validate(binding.nameFieldInput.text.toString())) {
                        is Result.Error -> {
                            when (result.error) {
                                ShopValidator.ShopError.NO_NAME -> {
                                    binding.nameField.error = getString(R.string.validation_no_name)
                                    binding.nameField.requestFocus()
                                }
                            }
                        }
                        is Result.Success -> {
                            // validation was successful
                            binding.nameField.error = null
                            binding.nameField.isErrorEnabled = false

                            viewModel.updateEntryAfterValidation()
                            viewModel.onValidatedAndUpdated()
                        }
                    }
                    binding.saveButton.isEnabled = true
                }
            }
        }

        viewModel.insertSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess != null && !isSuccess) {
                Snackbar.make(
                    binding.saveButton,
                    resources.getText(R.string.unique_product_violation),
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }

        binding.streetNameFieldInput.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.addShop()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                viewModel.eventFlow.collectLatest { event ->
                    when( event ) {
                        is AddShopViewModel.UiEvent.ShopCreated -> {
                            Toast.makeText( context, getString( R.string.add_shop_message_shop_created, event.shopName ), Toast.LENGTH_SHORT ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}