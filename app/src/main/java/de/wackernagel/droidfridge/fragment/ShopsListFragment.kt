package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.adapter.ShopsListAdapter
import de.wackernagel.droidfridge.databinding.FragmentShopsListBinding
import de.wackernagel.droidfridge.viewmodel.ShopsListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopsListFragment : BaseFragment() {

    private var _binding: FragmentShopsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShopsListViewModel>()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentShopsListBinding.inflate(inflater, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ShopsListAdapter { shopId -> viewModel.onAction(ShopsListViewModel.ShopsListAction.NavigateToShop(shopId)) }
        binding.shopsList.adapter = adapter
        binding.shopsList.layoutManager = GridLayoutManager( context, resources.getInteger( R.integer.grid_column_count ) )

        // avoid to expand or collapse CollapsingToolbarLayout by scrolling the RecyclerView
        ViewCompat.setNestedScrollingEnabled( binding.shopsList, false )

        viewModel.shops.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle( Lifecycle.State.STARTED ) {
                viewModel.uiEvent.collectLatest { event ->
                    when( event ) {
                        is ShopsListViewModel.UiEvent.NavigateToAddShop -> {
                            val toShopCreator = ShopsListFragmentDirections.actionShopsListFragmentToAddShopFragment()
                            findNavController().navigate( toShopCreator )
                        }

                        is ShopsListViewModel.UiEvent.NavigateToShop -> {
                            val toShopDetails = ShopsListFragmentDirections.actionShopsListFragmentToShopFragment( event.shopId )
                            findNavController().navigate( toShopDetails )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activateAddFAB { viewModel.onAction(ShopsListViewModel.ShopsListAction.NavigateToAddShop) }
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