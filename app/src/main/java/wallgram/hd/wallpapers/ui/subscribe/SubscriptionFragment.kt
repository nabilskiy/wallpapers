package wallgram.hd.wallpapers.ui.subscribe

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.*
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.databinding.FragmentSubscriptionBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.util.billing.DataWrappers
import wallgram.hd.wallpapers.util.billing.IapConnector
import wallgram.hd.wallpapers.util.billing.SubscriptionServiceListener
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.views.radiobutton.CustomRadioGroup
import wallgram.hd.wallpapers.views.radiobutton.OnCustomRadioButtonListener

class SubscriptionFragment : BaseFragment<BillingViewModel, FragmentSubscriptionBinding>(
    FragmentSubscriptionBinding::inflate
) {

    var selectedSku = YEAR_SKU

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subsList = listOf(MONTH_SKU, YEAR_SKU)

        val iapConnector = IapConnector(
            context = requireContext(),
            subscriptionKeys = subsList,
            key = BuildConfig.BILLING_KEY,
            enableLogging = true
        )

        iapConnector.addSubscriptionListener(object : SubscriptionServiceListener {
            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                Log.d("RESTORED", purchaseInfo.toString())
            }

            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                Log.d("PURCHASED", purchaseInfo.toString())
            }

            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.SkuDetails>) {
                Log.d("PRICES", iapKeyPrices.toString())
                binding?.apply{
                    monthSub.setPrice(iapKeyPrices.getValue(MONTH_SKU).price ?: "")
                    yearSub.setPrice(iapKeyPrices.getValue(YEAR_SKU).price ?: "")
                    progressBar.isVisible = false
                    subscribeContent.isVisible = true
                }

            }

        })

        with(binding) {
            subscribeBtn.setOnClickListener {
                iapConnector.subscribe(requireActivity(), selectedSku)
            }

            radioGroup.setOnClickListener(object : OnCustomRadioButtonListener {
                override fun onClick(view: View) {
                    text2.isInvisible = view.id != R.id.year_sub
                    selectedSku = if (view.id == R.id.month_sub) MONTH_SKU else YEAR_SKU
                }
            })

            radioGroup.setSelectedButtonToSelectedState(binding.yearSub)
        }
    }

    override fun getViewModel(): Class<BillingViewModel> = BillingViewModel::class.java
}