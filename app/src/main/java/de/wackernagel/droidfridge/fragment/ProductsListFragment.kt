package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import de.wackernagel.droidfridge.adapter.ProductAdapter
import de.wackernagel.droidfridge.databinding.FragmentProductListBinding
import de.wackernagel.droidfridge.viewmodel.ProductsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsListFragment: BaseFragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProductsListViewModel>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = ProductAdapter { productId ->
            viewModel.editItem(productId)
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        deactivateAddFAB()
    }
}