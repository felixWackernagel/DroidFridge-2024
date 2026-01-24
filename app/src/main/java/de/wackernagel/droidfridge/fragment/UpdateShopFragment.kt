package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.databinding.FragmentUpdateShopBinding
import de.wackernagel.droidfridge.di.UpdateShopViewModelFactory
import de.wackernagel.droidfridge.ui.Result
import de.wackernagel.droidfridge.ui.ShopValidator
import de.wackernagel.droidfridge.viewmodel.UpdateShopViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateShopFragment : BaseFragment(), MenuProvider {

    private var _binding : FragmentUpdateShopBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateShopFragmentArgs by navArgs()

    private val viewModel: UpdateShopViewModel by viewModels (
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<UpdateShopViewModelFactory> {
                    factory -> factory.create( args.shopId )
            }
        }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentUpdateShopBinding.inflate( inflater, container, false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindShop()

        setupListeners()

        observeEvents()

        requireActivity().addMenuProvider( this, viewLifecycleOwner, Lifecycle.State.RESUMED )

        // avoid to expand or collapse CollapsingToolbarLayout by scrolling the RecyclerView
        ViewCompat.setNestedScrollingEnabled( binding.nestedScrollView, false )
    }

    private fun bindShop() {
        val shop = viewModel.shop.value ?: return

        binding.shopNameField.setText(shop.name)
        binding.shopStreetField.setText(shop.street)
        binding.shopStreetnumberField.setText(shop.streetNumber)
        binding.shopPostalcodeField.setText(shop.postalCode)
        binding.shopCityField.setText(shop.city)
        binding.shopCountryField.setText(shop.country)
        binding.shopPhoneField.setText(shop.phone)
        binding.shopDetailsField.setText(shop.details)
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            val shop = viewModel.shop.value ?: return@setOnClickListener

            shop.apply {
                name = binding.shopNameField.text.toString()
                street = binding.shopStreetField.text.toString()
                streetNumber = binding.shopStreetnumberField.text.toString()
                postalCode = binding.shopPostalcodeField.text.toString()
                city = binding.shopCityField.text.toString()
                country = binding.shopCountryField.text.toString()
                phone = binding.shopPhoneField.text.toString()
                details = binding.shopDetailsField.text.toString()
            }

            viewModel.updateShop()
        }

        binding.shopDetailsField.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.updateShop()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                viewModel.eventFlow.collectLatest { event ->
                    when( event ) {
                        is UpdateShopViewModel.UiEvent.ShopUpdated -> {
                            Toast.makeText( context, getString( R.string.update_shop_message_shop_updated, event.shopName ), Toast.LENGTH_SHORT ).show()
                        }
                        is UpdateShopViewModel.UiEvent.ShopDeleted -> {
                            Toast.makeText( context, getString( R.string.update_shop_message_shop_deleted, event.shopName ), Toast.LENGTH_SHORT ).show()
                        }
                        is UpdateShopViewModel.UiEvent.ShopUpdateError -> {
                            Toast.makeText( context, R.string.update_shop_message_update_error, Toast.LENGTH_SHORT ).show()
                        }
                    }
                }
            }
        }

        viewModel.navigateToList.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_updateShopFragment_to_shopsListFragment)
                viewModel.onNavigatedToList()
            }
        }

        viewModel.navigateToDetailItem.observe(viewLifecycleOwner) { shopId ->
            shopId?.let {
                findNavController().navigate(R.id.action_updateShopFragment_to_shopFragment)
                viewModel.onShowItemDetailsNavigated()
            }
        }

        viewModel.doValidation.observe(viewLifecycleOwner) { doValidate ->
            if (doValidate) {
                lifecycleScope.launch {
                    binding.saveButton.isEnabled = false
                    when (val result =
                        ShopValidator().validate(binding.shopNameField.text.toString())) {
                        is Result.Error -> {
                            when (result.error) {
                                ShopValidator.ShopError.NO_NAME -> {
                                    binding.shopName.error = getString(R.string.update_shop_validation_name_required)
                                    binding.shopName.requestFocus()
                                }
                            }
                        }
                        is Result.Success -> {
                            // validation was successful
                            binding.shopName.error = null
                            binding.shopName.isErrorEnabled = false

                            viewModel.updateEntryAfterValidation()
                            viewModel.onValidatedAndUpdated()
                        }
                    }
                    binding.saveButton.isEnabled = true
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate( R.menu.menu_update_shop, menu )
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteShop -> {
                viewModel.deleteShop()
                return true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}