package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapter.ShopsListAdapter
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.databinding.FragmentShopsListBinding
import com.example.myapplication.viewmodel.ShopsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopsListFragment: BaseFragment() {

    private var _binding: FragmentShopsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShopsListViewModel>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentShopsListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = ShopsListAdapter { shopId ->
            viewModel.onItemClicked(shopId)
        }
        binding.shopsList.adapter = adapter
        activateAddFAB { viewModel.addItem() }

        viewModel.shops.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

//        viewModel.navigateToAddItem.observe(viewLifecycleOwner) { navigate ->
//            if (navigate) {
//                val action = ProductsListFragmentDirections
//                    .actionProductsListFragmentToAddProductFragment()
//                this.findNavController().navigate(action)
//                viewModel.onItemAddNavigated()
//            }
//        }
//
//        viewModel.navigateToEditItem.observe(viewLifecycleOwner) { productId ->
//            productId?.let {
//                val action = ProductsListFragmentDirections
//                    .actionProductsListFragmentToUpdateProductFragment(productId)
//                this.findNavController().navigate(action)
//                viewModel.onItemEditNavigated()
//            }
//        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        deactivateAddFAB()
    }
}