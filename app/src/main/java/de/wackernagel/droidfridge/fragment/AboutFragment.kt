package de.wackernagel.droidfridge.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.wackernagel.droidfridge.BuildConfig
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.databinding.FragmentAboutBinding
import de.wackernagel.droidfridge.ui.Helpers

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate( inflater, container, false )
        binding.appVersion.text = getString( R.string.about_app_version, BuildConfig.VERSION_NAME )
        binding.openSourceLicenses.setOnClickListener {
            OssLicensesMenuActivity.setActivityTitle( getString( R.string.licenses_activity_name ) )
            startActivity( Intent( requireActivity(), OssLicensesMenuActivity::class.java ) )
        }
        binding.dataProtection.setOnClickListener {
            it?.let {
                val webpage: Uri = Uri.parse("https://www.felixwackernagel.de/droidfridge/datenschutz")
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                Helpers.startIntentWhenAvailable( it.context, intent )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}