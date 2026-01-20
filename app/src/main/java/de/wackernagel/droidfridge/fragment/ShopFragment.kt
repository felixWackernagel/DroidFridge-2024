package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.databinding.FragmentShopBinding
import de.wackernagel.droidfridge.di.ShopViewModelFactory
import de.wackernagel.droidfridge.viewmodel.ShopViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopFragment : BaseFragment(), MenuProvider {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShopViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<ShopViewModelFactory> {
                factory -> factory.create( ShopFragmentArgs.fromBundle( requireArguments() ).shopId )
            }
        }
    )

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentShopBinding.inflate( inflater, container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider( this, viewLifecycleOwner, Lifecycle.State.RESUMED )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                viewModel.eventFlow.collectLatest { event ->
                    when( event ) {
                        is ShopViewModel.UiEvent.MarkedAsFavorite -> {
                            Toast.makeText( context, getString( R.string.shop_message_add_to_favorites, event.shopName ), Toast.LENGTH_SHORT ).show()
                        }
                        is ShopViewModel.UiEvent.UnmarkedAsFavorite -> {
                            Toast.makeText( context, getString( R.string.shop_message_removed_from_favorites, event.shopName ), Toast.LENGTH_SHORT ).show()
                        }
                        is ShopViewModel.UiEvent.ShopDeleted -> {
                            Toast.makeText( context, getString( R.string.shop_message_shop_deleted, event.shopName ), Toast.LENGTH_SHORT ).show()
                        }
                    }
                }
            }
        }

        viewModel.shop.observe( viewLifecycleOwner ) { shopWithOpeningHours ->
            val shop = shopWithOpeningHours.shop

            binding.shopName.text = shop.name
            binding.shopAddress.text = shop.street
            binding.shopPhone.text = shop.phone
            binding.shopDetails.text = shop.details

            binding.shopPhone.isVisible = !shop.phone.isNullOrBlank()
            binding.shopDetails.isVisible = !shop.details.isNullOrBlank()

            binding.shopAddress.setOnClickListener {
                viewModel.openMap( view.context, shop )
            }

            binding.shopPhone.setOnClickListener {
                viewModel.dialPhoneNumber(view.context, shop)
            }

            val address = buildAddress(shop)
            binding.shopAddress.text = address
            binding.shopAddress.isVisible = address.isNotEmpty()

            activateFavoriteFAB(
                { viewModel.toggleFavoriteShop() },
                shop.isFavorite
            )
        }

        viewModel.navigateToEditItem.observe(viewLifecycleOwner) { shopId ->
            shopId?.let {
                val toUpdateShop = ShopFragmentDirections.actionShopFragmentToUpdateShopFragment( shopId )
                findNavController().navigate( toUpdateShop )
                viewModel.onItemEditNavigated()
            }
        }
        viewModel.navigateToList.observe(viewLifecycleOwner) { navigateToListItems ->
            if( navigateToListItems ) {
                findNavController().navigate( ShopFragmentDirections.actionShopFragmentToShopsListFragment() )
                viewModel.onNavigatedToList()
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate( R.menu.menu_shop, menu )
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.editShop -> {
                viewModel.navigateToEditor()
                true
            }
            R.id.deleteShop -> {
                viewModel.deleteShop()
                return true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        activateFavoriteFAB(
            { viewModel.toggleFavoriteShop() },
            viewModel.shop.value?.shop?.isFavorite ?: false
        )
    }

    override fun onPause() {
        super.onPause()
        deactivateFavoriteFAB()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun buildAddress( shop: Shop ): String {
        val line1 = StringBuilder()
        if( !TextUtils.isEmpty( shop.street ) ) line1.append( shop.street )
        if( !TextUtils.isEmpty( shop.streetNumber ) ) {
            if( line1.isNotEmpty() ) line1.append( " " )
            line1.append( shop.streetNumber )
        }
        val line2 = StringBuilder()
        if( !TextUtils.isEmpty( shop.postalCode ) ) line2.append( shop.postalCode )
        if( !TextUtils.isEmpty( shop.city ) ) {
            if( line2.isNotEmpty() ) line2.append( " " )
            line2.append( shop.city )
        }
        val address = StringBuilder()
        if( line1.isNotEmpty() ) address.append( line1.toString() )
        if( line1.isNotEmpty() && line2.isNotEmpty() ) address.append( "\n" )
        if( line2.isNotEmpty() ) address.append( line2.toString() )
        if( address.isNotEmpty() && !TextUtils.isEmpty( shop.country ) ) address.append( "\n" )
        if( !TextUtils.isEmpty( shop.country ) ) address.append( shop.country )

        return address.toString()
    }
}