package wallgram.hd.wallpapers.ui.subscribe

import android.os.Bundle
import android.view.View
import wallgram.hd.wallpapers.databinding.FragmentSubscriptionBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment

class SubscriptionFragment : BaseFragment<BillingViewModel, FragmentSubscriptionBinding>(
        FragmentSubscriptionBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            subscribeBtn.setOnClickListener {
                viewModel.buyMonth()
            }
        }
    }

    override fun getViewModel(): Class<BillingViewModel> = BillingViewModel::class.java
}