package wallgram.hd.wallpapers.ui.subscribe

import android.os.Bundle
import android.view.View
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.MONTH_SKU
import wallgram.hd.wallpapers.SIX_SKU
import wallgram.hd.wallpapers.YEAR_SKU
import wallgram.hd.wallpapers.databinding.FragmentSubscriptionBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.util.billing.IapConnector
import wallgram.hd.wallpapers.util.modo.back

class SubscriptionFragment : BaseFragment<BillingViewModel, FragmentSubscriptionBinding>(
        FragmentSubscriptionBinding::inflate
) {

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subsList = listOf(MONTH_SKU, SIX_SKU, YEAR_SKU)

        val iapConnector = IapConnector(
            context = requireContext(),
            subscriptionKeys = subsList,
            enableLogging = true
        )

        with(binding){
            subscribeBtn.setOnClickListener {
                iapConnector.subscribe(requireActivity(), MONTH_SKU)
            }
        }
    }

    override fun getViewModel(): Class<BillingViewModel> = BillingViewModel::class.java
}