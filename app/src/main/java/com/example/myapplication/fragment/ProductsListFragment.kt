package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MyApplication
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.databinding.FragmentProductListBinding
import com.example.myapplication.viewmodel.ProductsListViewModel

class ProductsListFragment: BaseFragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(activity).application
        val productRepository = ( application as MyApplication ).productRepository
        val viewModelFactory = ProductsListViewModel.Factory(productRepository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[ProductsListViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = ProductAdapter { productId ->
            viewModel.onItemClicked(productId)
        }
        binding.productsList.adapter = adapter
        activateAddFAB { viewModel.addItem() }

        viewModel.products.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToAddItem.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                val action = ProductsListFragmentDirections
                    .actionProductsListFragmentToAddProductFragment()
                this.findNavController().navigate(action)
                viewModel.onItemAddNavigated()
            }
        }

        viewModel.navigateToEditItem.observe(viewLifecycleOwner) { productId ->
            productId?.let {
                val action = ProductsListFragmentDirections
                    .actionProductsListFragmentToUpdateProductFragment(productId)
                this.findNavController().navigate(action)
                viewModel.onItemEditNavigated()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        deactivateAddFAB()
    }
}