package wallgram.hd.wallpapers.views

import android.webkit.WebView
import android.os.Bundle
import wallgram.hd.wallpapers.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.webkit.WebResourceRequest
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import wallgram.hd.wallpapers.databinding.AgreementDialogBinding
import wallgram.hd.wallpapers.util.Common

class AgreementDialog : DialogFragment() {

    private var binding: AgreementDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AgreementDialogBinding.inflate(inflater, container, false)

        return binding?.root
    }

    private fun loadPage() {
        binding?.apply {
            webView.apply {
                loadUrl(Common.getSiteUrl() + "terms")
                webViewClient = mWebViewClient
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPage()

        binding?.apply {
            toolbar.apply {
                setNavigationOnClickListener {
                    dismiss()
                }
                setTitle(R.string.privacy_policy)
                setOnMenuItemClickListener {
                    dismiss()
                    true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            if (dialog.window != null) {
                dialog.window!!.setLayout(width, height)
                dialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
            }
        }
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding?.apply {
                progressBar.isVisible = false
            }
        }
    }

    companion object {
        private const val TAG = "agreement_dialog"
        fun display(fragmentManager: FragmentManager) {
            val agreementDialog = AgreementDialog()
            agreementDialog.show(fragmentManager, TAG)
        }
    }
}