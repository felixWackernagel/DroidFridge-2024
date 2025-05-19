package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.wackernagel.droidfridge.R
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
            viewModel.showItemDetails(shopId)
        }
        binding.shopsList.adapter = adapter
        binding.shopsList.layoutManager = GridLayoutManager( context, resources.getInteger( R.integer.grid_column_count ) )

        // avoid to expand or collapse CollapsingToolbarLayout by scrolling the RecyclerView
        ViewCompat.setNestedScrollingEnabled( binding.shopsList, false )

        viewModel.shops.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToAddItem.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                val toShopCreator = ShopsListFragmentDirections.actionShopsListFragmentToAddShopFragment()
                findNavController().navigate( toShopCreator )
                viewModel.onItemAddNavigated()
            }
        }

        viewModel.navigateToDetailItem.observe(viewLifecycleOwner) { shopId ->
            shopId?.let {
                val toShopDetails = ShopsListFragmentDirections.actionShopsListFragmentToShopFragment( shopId )
                findNavController().navigate( toShopDetails )
                viewModel.onShowItemDetailsNavigated()
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