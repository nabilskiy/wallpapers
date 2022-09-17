package wallgram.hd.wallpapers.presentation.subscribe

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.presentation.SingleLiveEvent
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    dispatchers: Dispatchers
): BaseViewModel(dispatchers) {

    private val _dataState = MutableLiveData<SubscriptionUi>()
    val dataState: LiveData<SubscriptionUi>
        get() = _dataState


    val openPlayStoreSubscriptionsEvent = SingleLiveEvent<String>()

    fun buySku(activity: Activity, sku: String, currentSku: String) {
        if(sku == currentSku){
            openPlayStoreSubscriptionsEvent.postValue(sku)
            return
        }
        billingRepository.buySku(activity, sku)
    }

    fun fetch() {
        val listSubs = listOf(
            Subscription.MONTH(billingRepository),
            Subscription.YEAR(billingRepository)
        )
        _dataState.value = SubscriptionUi.Base(listSubs)
    }

    fun init() {
        fetch()
    }



}
