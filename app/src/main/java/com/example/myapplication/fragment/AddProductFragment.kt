package com.example.myapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.myapplication.MyApplication
import com.example.myapplication.R
import com.example.myapplication.data.Validation
import com.example.myapplication.databinding.FragmentAddProductBinding
import com.example.myapplication.ui.formfields.FormFieldText
import com.example.myapplication.viewmodel.AddProductViewModel
import com.google.android.material.snackbar.Snackbar

class AddProductFragment : BaseFragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(activity).application
        val productRepository = ( application as MyApplication).productRepository
        val viewModelFactory = AddProductViewModel.Factory(productRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AddProductViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val fieldName = FormFieldText(
            scope = lifecycleScope,
            textInputLayout = binding.nameTextFieldBinding.nameLayout,
            textInputEditText = binding.nameTextFieldBinding.name,
            Validation.nameValidation
        )
        val formFields = listOf(fieldName)
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController().navigate(R.id.action_addProductFragment_to_productsListFragment)
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
                Log.d(TAG,"There was a problem when inserting product data to the database ...")
            }
        })
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}