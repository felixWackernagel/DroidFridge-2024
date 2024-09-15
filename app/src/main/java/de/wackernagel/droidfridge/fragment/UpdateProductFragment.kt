package de.wackernagel.droidfridge.fragment

import de.wackernagel.droidfridge.databinding.FragmentUpdateProductBinding
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.Validation
import de.wackernagel.droidfridge.di.UpdateProductViewModelFactory
import de.wackernagel.droidfridge.ui.formfields.FormFieldText
import de.wackernagel.droidfridge.viewmodel.UpdateProductViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

@AndroidEntryPoint
class UpdateProductFragment: BaseFragment() {

    private var _binding: FragmentUpdateProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UpdateProductViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<UpdateProductViewModelFactory> {
                factory -> factory.create( UpdateProductFragmentArgs.fromBundle(requireArguments()).productId )
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val fieldTitle = FormFieldText(
            scope = lifecycleScope,
            textInputLayout = binding.titleTextFieldBinding.nameLayout,
            textInputEditText = binding.titleTextFieldBinding.name,
            Validation.nameValidation
        )
        val formFields = listOf(fieldTitle)
        saveToUpdateButton(binding.crudButtons.actionButton)
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_updateProductFragment_to_productsListFragment)
                viewModel.onNavigatedToList()
            }
        })
        viewModel.doValidation.observe(viewLifecycleOwner, Observer { doValidate ->
            if (doValidate) {
                formValidation(binding.crudButtons.actionButton, formFields, viewModel)
            }
        })
        viewModel.insertSuccess.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess != null && !isSuccess ) {
                Snackbar.make(binding.crudButtons.actionButton,resources.getText(R.string.unique_product_violation),
                    Snackbar.LENGTH_INDEFINITE).show()
                Log.d("UpdateProductFragment","There was a problem when updating gender data in the database ...")
            }
        })
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}