package de.wackernagel.droidfridge.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import de.wackernagel.droidfridge.databinding.FragmentUpdateShopBinding
import de.wackernagel.droidfridge.di.UpdateShopViewModelFactory
import de.wackernagel.droidfridge.viewmodel.UpdateShopViewModel

@AndroidEntryPoint
class UpdateShopFragment : BaseFragment() {

    private var _binding : FragmentUpdateShopBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UpdateShopViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<UpdateShopViewModelFactory> {
                    factory -> factory.create( UpdateShopFragmentArgs.fromBundle( requireArguments() ).shopId )
            }
        }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentUpdateShopBinding.inflate( inflater, container, false )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}