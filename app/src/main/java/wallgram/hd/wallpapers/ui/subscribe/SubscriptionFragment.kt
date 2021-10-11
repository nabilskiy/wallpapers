package wallgram.hd.wallpapers.ui.subscribe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.*
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.databinding.FragmentSubscriptionBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.util.localization.LanguageSetting
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.views.radiobutton.CustomRadioGroup
import wallgram.hd.wallpapers.views.radiobutton.OnCustomRadioButtonListener
import java.util.*

class SubscriptionFragment : BaseFragment<BillingViewModel, FragmentSubscriptionBinding>(
    FragmentSubscriptionBinding::inflate
) {

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    private var currentSub = DEFAULT_SKU

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currency = Currency.getInstance(LanguageSetting.getDefaultLanguage(App.context)).symbol

        viewModel.openPlayStoreSubscriptionsEvent.observe(viewLifecycleOwner, { sku ->
            openPlayStoreSubscriptions(sku)
        })

        viewModel.getSkuDetails(MONTH_SKU).price.observe(viewLifecycleOwner, {
            binding.monthSub.setPrice(it)
            if(it.isNotEmpty()){
                hideLoading()
            }
        })

        viewModel.getSkuDetails(YEAR_SKU).price.observe(viewLifecycleOwner, {
            binding.yearSub.setPrice(it)
            if(it.isNotEmpty()){
                hideLoading()
            }
        })

        viewModel.getCurrentSub().observe(viewLifecycleOwner, {

            if(currentSub == it){
                hideLoading()
                return@observe
            }

            showLoading()
            currentSub = it
            updateButtonBackground(it)
            updateButtonCurrent(it)
        })

        with(binding) {

            defaultSku.setPrice("0 $currency")

            subscribeBtn.setOnClickListener {
                val sub = when(radioGroup.selectedButton){
                    R.id.default_sku -> currentSub
                    R.id.month_sub -> MONTH_SKU
                    R.id.year_sub -> YEAR_SKU
                    else -> currentSub
                }
                viewModel.buySku(requireActivity(), sub, currentSub)
            }

            radioGroup.setOnClickListener(object : OnCustomRadioButtonListener {
                override fun onClick(view: View) {
                    text2.isInvisible = view.id != R.id.year_sub
                }
            })
        }
    }

    private fun hideLoading() {
        binding.subscribeContent.isVisible = true
        binding.progressBar.isVisible = false
    }


    private fun showLoading() {
        binding.subscribeContent.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun updateButtonBackground(sub: String) {
        with(binding){
            radioGroup.setSelectedButton(when(sub){
                MONTH_SKU -> monthSub
                else -> yearSub
            })
            text2.isInvisible = sub != YEAR_SKU
        }
    }

    private fun updateButtonCurrent(sub: String) {
        with(binding){
            radioGroup.setCurrent(when(sub){
                MONTH_SKU -> monthSub
                YEAR_SKU -> yearSub
                else -> defaultSku
            })
        }
        hideLoading()
    }


    private fun openPlayStoreSubscriptions(sku: String) {
        if(sku == DEFAULT_SKU)
            return

        val url = String.format(PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL, sku, requireActivity().packageName)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun getViewModel(): Class<BillingViewModel> = BillingViewModel::class.java
}