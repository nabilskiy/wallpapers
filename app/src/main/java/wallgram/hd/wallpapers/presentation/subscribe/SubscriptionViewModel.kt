package wallgram.hd.wallpapers.presentation.subscribe

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.presentation.SingleLiveEvent
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.subscribe.buy.BuySubscriptions
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    private val resourceProvider: ResourceProvider,
    private val updateBuySubscriptions: BuySubscriptions.Observe,
    private val updateSubscriptions: UpdateSubscriptions.Observe,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private val _dataState = MutableLiveData<SubscriptionUi>()
    val dataState: LiveData<SubscriptionUi>
        get() = _dataState


    val openPlayStoreSubscriptionsEvent = SingleLiveEvent<String>()

    fun buySku(activity: Activity, sku: String, currentSku: String = "") {
        if (sku == currentSku) {
            openPlayStoreSubscriptionsEvent.postValue(sku)
            return
        }
        billingRepository.buySku(activity, sku)
    }

    fun buySubscription(lifecycleOwner: LifecycleOwner, observer: Observer<String>) {
        updateBuySubscriptions.observe(lifecycleOwner, observer)
    }

    fun updateSubscriptions(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        updateSubscriptions.observe(lifecycleOwner, observer)
    }

    fun load(): LiveData<SubscriptionsUi> {
        val subscriptions = billingRepository.getSubscriptions()
        return subscriptions.map {
            it.map(SubscriptionsDomain.Mapper.Base(resourceProvider))
        }
    }

    fun fetch(lifecycleOwner: LifecycleOwner, observer: Observer<SubscriptionsUi>) {
        val data = load()

        data.observe(lifecycleOwner, observer)

        // _dataState.value = SubscriptionUi.Base(listSubs)
    }

//    fun init() {
//        fetch()
//    }


}
