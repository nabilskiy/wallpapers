package wallgram.hd.wallpapers.presentation.subscribe

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ActivityContext
import wallgram.hd.wallpapers.data.billing.BillingRepository
import javax.inject.Inject

interface Subscribe {

    fun buySku(sku: String)

    class Base @Inject constructor(
        @ActivityContext
        private val activity: Context,
        private val billingRepository: BillingRepository
    ) : Subscribe {
        override fun buySku(sku: String) {
            billingRepository.buySku(activity as FragmentActivity, sku)
        }

    }

}