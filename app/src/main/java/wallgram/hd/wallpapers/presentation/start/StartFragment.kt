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
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.databinding.FragmentStartBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.util.modo.replace

class StartFragment :
    BaseFragment<StartViewModel, FragmentStartBinding>(FragmentStartBinding::inflate) {

    private val modo = wallgram.hd.wallpapers.App.modo


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val widthPx = requireContext().physicalScreenRectPx.width()
//        val heightPx = requireContext().physicalScreenRectPx.height()
        // binding.resolutionText.text = "$widthPx x $heightPx"

        binding.purchaseButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionResult.launch(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
                // preferences.save(FIRST_LAUNCH, false)
                modo.replace(Screens.MultiStack())
            } else {
                //  preferences.save(FIRST_LAUNCH, false)
                modo.replace(Screens.MultiStack())
            }
        }
        setUserAgreement()
    }

    private val mPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

        }

    private fun setUserAgreement() {
        clicking(
            binding.userAgreement,
            resources.getString(R.string.agreement_sub),
            object : ClickSpan.OnClickListener {
                override fun onClick() {
                    wallgram.hd.wallpapers.views.AgreementDialog.display(childFragmentManager)
                }
            })
    }

    class ClickSpan internal constructor(private val mListener: OnClickListener) : ClickableSpan() {
        override fun onClick(widget: View) {
            mListener.onClick()
        }

        interface OnClickListener {
            fun onClick()
        }
    }

    companion object {
        fun clicking(
            view: TextView, clickableText: String,
            listener: ClickSpan.OnClickListener
        ) {
            val text = view.text
            val string = text.toString()
            val span = ClickSpan(listener)
            val start = string.indexOf(clickableText)
            val end = start + clickableText.length
            if (start == -1) return
            if (text is Spannable) {
                text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                val s = SpannableString.valueOf(text)
                s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.text = s
            }
            val m = view.movementMethod
            if (m !is LinkMovementMethod) {
                view.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    override val viewModel: StartViewModel by viewModels()
}