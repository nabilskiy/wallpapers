package wallgram.hd.wallpapers.data.repository.billing

import androidx.lifecycle.MediatorLiveData
import wallgram.hd.wallpapers.model.SubscriptionStatus

interface BillingRepositorySource {

    fun getSubscription(): MediatorLiveData<List<SubscriptionStatus>>
}