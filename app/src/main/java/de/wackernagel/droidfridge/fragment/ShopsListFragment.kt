package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.wackernagel.droidfridge.adapter.ShopsListAdapter
import de.wackernagel.droidfridge.databinding.FragmentShopsListBinding
import de.wackernagel.droidfridge.viewmodel.ShopsListViewModel


@AndroidEntryPoint
class ShopsListFragment : BaseFragment() {

    private var _binding: FragmentShopsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShopsListViewModel>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentShopsListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = ShopsListAdapter { shopId ->
            viewModel.onItemClicked(shopId)
        }
        binding.shopsList.adapter = adapter

        viewModel.shops.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToAddItem.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                val action = ShopsListFragmentDirections.actionShopsListFragmentToAddShopFragment()
                findNavController().navigate( action )
                viewModel.onItemAddNavigated()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activateAddFAB { viewModel.addItem() }
    }

    override fun onPause() {
        super.onPause()
        deactivateAddFAB()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}