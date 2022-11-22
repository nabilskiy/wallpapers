package wallgram.hd.wallpapers.presentation.start

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.databinding.FragmentStartBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.main.FirstLaunch
import wallgram.hd.wallpapers.util.makeClickable
import wallgram.hd.wallpapers.util.modo.replace
import wallgram.hd.wallpapers.views.AgreementDialog
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment :
    BaseFragment<StartViewModel, FragmentStartBinding>(FragmentStartBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayPermission = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        binding.continueButton.setOnClickListener {
            if (isPermissionsDenied())
                mPermissionResult.launch(arrayPermission)
            viewModel.navigateHome()
        }

        setUserAgreement()

        viewModel.observe(viewLifecycleOwner) {
            it.show(binding.deviceName, binding.resolutionText)
        }
    }

    private fun isPermissionsDenied() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) != PackageManager.PERMISSION_GRANTED

    private val mPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

        }

    private fun setUserAgreement() {
        binding.userAgreement.makeClickable(
            getString(R.string.agreement_sub)
        ) { AgreementDialog.display(childFragmentManager) }

    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_status_bar)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    override val viewModel: StartViewModel by viewModels()
}