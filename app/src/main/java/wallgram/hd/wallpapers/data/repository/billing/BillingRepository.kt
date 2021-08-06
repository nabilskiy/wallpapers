
package wallgram.hd.wallpapers.data.repository.billing

import androidx.lifecycle.MediatorLiveData
import com.android.billingclient.api.Purchase
import wallgram.hd.wallpapers.model.ContentResource
import wallgram.hd.wallpapers.model.SubscriptionStatus
import wallgram.hd.wallpapers.util.billing.BillingClientLifecycle
import javax.inject.Inject

/**
 * Repository handling the work with subscriptions.
 */
class BillingRepository @Inject constructor(
        private val localDataSource: LocalDataSource,
        private val billingClientLifecycle: BillingClientLifecycle
): BillingRepositorySource {

    /**
     * Live data is true when there are pending network requests.
     */
    /**
     * [MediatorLiveData] to coordinate updates from the database and the network.
     *
     * The mediator observes multiple sources. The database source is immediately exposed.
     * The network source is stored in the database, which will eventually be exposed.
     * The mediator provides an easy way for us to use LiveData for both the local data source
     * and the network data source, without implementing a new callback interface.
     */
    val subscriptions = MediatorLiveData<List<SubscriptionStatus>>()

    /**
     * Live Data with the basic content.
     */
    val basicContent = MediatorLiveData<ContentResource>()

    /**
     * Live Data with the premium content.
     */
    val premiumContent = MediatorLiveData<ContentResource>()

    init {

        // When the list of purchases changes, we need to update the subscription status
        // to indicate whether the subscription is local or not. It is local if the
        // the Google Play Billing APIs return a Purchase record for the SKU. It is not
        // local if there is no record of the subscription on the device.
        subscriptions.addSource(billingClientLifecycle.purchases) {
            // We only need to update the database if the isLocalPurchase field needs to change.
            val purchases = it
            subscriptions.value?.let {
                val subscriptions = it
                val hasChanged = updateLocalPurchaseTokens(subscriptions, purchases)
                if (hasChanged) {
                    localDataSource.updateSubscriptions(subscriptions)
                }
            }
        }
    }

    /**
     * Acknowledge subscriptions that have been registered by the server.
     */
    private fun acknowledgeRegisteredPurchaseTokens(
            remoteSubscriptions: List<SubscriptionStatus>
    ) {
        for (remoteSubscription in remoteSubscriptions) {
            remoteSubscription.purchaseToken?.let { purchaseToken ->
                billingClientLifecycle.acknowledgePurchase(purchaseToken)
            }
        }
    }

    /**
     * Merge the previous subscriptions and new subscriptions by looking at on-device purchases.
     *
     * We want to return the list of new subscriptions, possibly with some modifications
     * based on old subscriptions and the on-devices purchases from Google Play Billing.
     * Old subscriptions should be retained if they are owned by someone else (subAlreadyOwned)
     * and the purchase token for the subscription is still on this device.
     */
    private fun mergeSubscriptionsAndPurchases(
            oldSubscriptions: List<SubscriptionStatus>?,
            newSubscriptions: List<SubscriptionStatus>?,
            purchases: List<Purchase>?
    ): List<SubscriptionStatus> {
        return ArrayList<SubscriptionStatus>().apply {
            if (purchases != null) {
                // Record which purchases are local and can be managed on this device.
                updateLocalPurchaseTokens(newSubscriptions, purchases)
            }
            if (newSubscriptions != null) {
                addAll(newSubscriptions)
            }
            // Find old subscriptions that are in purchases but not in new subscriptions.
            if (purchases != null && oldSubscriptions != null) {
                for (oldSubscription in oldSubscriptions) {
                    if (oldSubscription.subAlreadyOwned && oldSubscription.isLocalPurchase) {
                        // This old subscription was previously marked as "already owned" by
                        // another user. It should be included in the output if the SKU
                        // and purchase token match their previous value.
                        for (purchase in purchases) {
                            if (purchase.skus[0] == oldSubscription.sku &&
                                    purchase.purchaseToken == oldSubscription.purchaseToken) {
                                // The old subscription that was already owned subscription should
                                // be added to the new subscriptions.
                                // Look through the new subscriptions to see if it is there.
                                var foundNewSubscription = false
                                newSubscriptions?.let {
                                    for (newSubscription in it) {
                                        if (newSubscription.sku == oldSubscription.sku) {
                                            foundNewSubscription = true
                                        }
                                    }
                                }
                                if (!foundNewSubscription) {
                                    // The old subscription should be added to the output.
                                    // It matches a local purchase.
                                    add(oldSubscription)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Modify the subscriptions isLocalPurchase field based on the list of local purchases.
     * Return true if any of the values changed.
     */
    private fun updateLocalPurchaseTokens(
            subscriptions: List<SubscriptionStatus>?,
            purchases: List<Purchase>?
    ): Boolean {
        var hasChanged = false
        subscriptions?.let {
            for (subscription in it) {
                var isLocalPurchase = false
                var purchaseToken = subscription.purchaseToken
                purchases?.let {
                    for (purchase in it) {
                        if (subscription.sku == purchase.skus[0]) {
                            isLocalPurchase = true
                            purchaseToken = purchase.purchaseToken
                        }
                    }
                }
                if (subscription.isLocalPurchase != isLocalPurchase) {
                    subscription.isLocalPurchase = isLocalPurchase
                    subscription.purchaseToken = purchaseToken
                    hasChanged = true
                }
            }
        }
        return hasChanged
    }

    /**
     * Delete local user data when the user signs out.
     */
    fun deleteLocalUserData() {
        localDataSource.deleteLocalUserData()
        basicContent.postValue(null)
        premiumContent.postValue(null)
    }

    override fun getSubscription(): MediatorLiveData<List<SubscriptionStatus>> = subscriptions

}
