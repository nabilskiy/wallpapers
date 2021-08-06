package wallgram.hd.wallpapers.ui.subscribe

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.MONTH_SKU
import wallgram.hd.wallpapers.SIX_SKU
import wallgram.hd.wallpapers.YEAR_SKU
import wallgram.hd.wallpapers.data.repository.billing.BillingRepositorySource
import wallgram.hd.wallpapers.util.SingleLiveEvent
import wallgram.hd.wallpapers.util.billing.deviceHasGooglePlaySubscription
import wallgram.hd.wallpapers.util.billing.purchaseForSku
import wallgram.hd.wallpapers.util.billing.subscriptionForSku
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import wallgram.hd.wallpapers.model.SubscriptionStatus
import javax.inject.Inject

class BillingViewModel @Inject constructor(
                                           val billingRepository: BillingRepositorySource) : AndroidViewModel(wallgram.hd.wallpapers.App()) {

    /**
     * Local billing purchase data.
     */
    private val purchases = wallgram.hd.wallpapers.App().billingClientLifecycle.purchases

    /**
     * SkuDetails for all known SKUs.
     */
    private val skusWithSkuDetails =
            wallgram.hd.wallpapers.App().billingClientLifecycle.skusWithSkuDetails

    /**
     * Subscriptions record according to the server.
     */
    private val subscriptions = billingRepository.getSubscription()

    /**
     * Send an event when the Activity needs to buy something.
     */
    val buyEvent = SingleLiveEvent<BillingFlowParams>()

    /**
     * Send an event when the UI should open the Google Play
     * Store for the user to manage their subscriptions.
     */
    val openPlayStoreSubscriptionsEvent = SingleLiveEvent<String>()

    /**
     * Open the Play Store subscription center. If the user has exactly one SKU,
     * then open the deeplink to the specific SKU.
     */
    fun openPlayStoreSubscriptions() {
        val hasMonth = deviceHasGooglePlaySubscription(purchases.value, MONTH_SKU)
        val hasSix = deviceHasGooglePlaySubscription(purchases.value, SIX_SKU)
        val hasYear = deviceHasGooglePlaySubscription(purchases.value, YEAR_SKU)

        Log.d("Billing", "hasBasic: $hasMonth, hasPremium: $hasSix")
        when {
            hasMonth && !hasSix && !hasYear -> {
                // If we just have a basic subscription, open the basic SKU.
                openPlayStoreSubscriptionsEvent.postValue(MONTH_SKU)
            }
            !hasMonth && hasSix && !hasYear -> {
                // If we just have a premium subscription, open the premium SKU.
                openPlayStoreSubscriptionsEvent.postValue(SIX_SKU)
            }
            !hasMonth && !hasSix && hasYear -> {
                // If we just have a premium subscription, open the premium SKU.
                openPlayStoreSubscriptionsEvent.postValue(YEAR_SKU)
            }
            else -> {
                // If we do not have an active subscription,
                // or if we have multiple subscriptions, open the default subscription center.
                openPlayStoreSubscriptionsEvent.call()
            }
        }
    }

    /**
     * Open the subscription page on Google Play.
     *
     * Since the purchase tokens will not be returned during account hold or pause,
     * we use the server data to determine the deeplink to Google Play.
     */
    fun openSubscriptionPageOnGooglePlay() {
//        val isPremiumOnServer = serverHasSubscription(subscriptions.value, Constants.PREMIUM_SKU)
//        val isBasicOnServer = serverHasSubscription(subscriptions.value, Constants.BASIC_SKU)
//        when {
//            isPremiumOnServer -> openPremiumPlayStoreSubscriptions()
//            isBasicOnServer -> openBasicPlayStoreSubscriptions()
//        }
    }

    /**
     * Open the Play Store basic subscription.
     */
    fun openMonthPlayStoreSubscriptions() {
        openPlayStoreSubscriptionsEvent.postValue(MONTH_SKU)
    }

    /**
     * Open the Play Store premium subscription.
     */
    fun openSixPlayStoreSubscriptions() {
        openPlayStoreSubscriptionsEvent.postValue(SIX_SKU)
    }

    fun openYearPlayStoreSubscriptions() {
        openPlayStoreSubscriptionsEvent.postValue(YEAR_SKU)
    }

    /**
     * Buy a basic subscription.
     */
    fun buyMonth() {
        val hasMonth = deviceHasGooglePlaySubscription(purchases.value, MONTH_SKU)
        val hasSix = deviceHasGooglePlaySubscription(purchases.value, SIX_SKU)
        val hasYear = deviceHasGooglePlaySubscription(purchases.value, YEAR_SKU)
        Log.d("Billing", "hasBasic: $hasMonth, hasPremium: $hasSix")
        when {
            hasMonth && hasSix && hasYear -> {
                // If the user has both subscriptions, open the basic SKU on Google Play.
                openPlayStoreSubscriptionsEvent.postValue(MONTH_SKU)
            }
            hasMonth && !hasSix && !hasYear -> {
                // If the user just has a basic subscription, open the basic SKU on Google Play.
                openPlayStoreSubscriptionsEvent.postValue(MONTH_SKU)
            }
            !hasMonth && hasSix && !hasYear -> {
                // If the user just has a premium subscription, downgrade.
                buy(sku = MONTH_SKU, oldSku = SIX_SKU)
            }
            !hasMonth && !hasSix && hasYear -> {
                // If the user just has a premium subscription, downgrade.
                buy(sku = MONTH_SKU, oldSku = YEAR_SKU)
            }
            else -> {
                // If the user dooes not have a subscription, buy the basic SKU.
                buy(sku = MONTH_SKU, oldSku = null)
            }
        }
    }

    /**
     * Buy a premium subscription.
     */
    fun buySixMonth() {
        val hasMonth = deviceHasGooglePlaySubscription(purchases.value, MONTH_SKU)
        val hasSix = deviceHasGooglePlaySubscription(purchases.value, SIX_SKU)
        val hasYear = deviceHasGooglePlaySubscription(purchases.value, YEAR_SKU)
        Log.d("Billing", "hasBasic: $hasMonth, hasPremium: $hasSix")
        when {
            hasMonth && hasSix && hasYear -> {
                // If the user has both subscriptions, open the basic SKU on Google Play.
                openPlayStoreSubscriptionsEvent.postValue(SIX_SKU)
            }
            !hasMonth && hasSix && !hasYear -> {
                // If the user just has a basic subscription, open the basic SKU on Google Play.
                openPlayStoreSubscriptionsEvent.postValue(SIX_SKU)
            }
            hasMonth && !hasSix && !hasYear -> {
                // If the user just has a premium subscription, downgrade.
                buy(sku = SIX_SKU, oldSku = MONTH_SKU)
            }
            !hasMonth && !hasSix && hasYear -> {
                // If the user just has a premium subscription, downgrade.
                buy(sku = SIX_SKU, oldSku = YEAR_SKU)
            }
            else -> {
                // If the user dooes not have a subscription, buy the basic SKU.
                buy(sku = SIX_SKU, oldSku = null)
            }
        }
    }

    fun buyYear() {
        val hasMonth = deviceHasGooglePlaySubscription(purchases.value, MONTH_SKU)
        val hasSix = deviceHasGooglePlaySubscription(purchases.value, SIX_SKU)
        val hasYear = deviceHasGooglePlaySubscription(purchases.value, YEAR_SKU)
        Log.d("Billing", "hasBasic: $hasMonth, hasPremium: $hasSix")
        when {
            hasMonth && hasSix && hasYear -> {
                // If the user has both subscriptions, open the basic SKU on Google Play.
                openPlayStoreSubscriptionsEvent.postValue(YEAR_SKU)
            }
            hasMonth && !hasSix && !hasYear -> {
                // If the user just has a basic subscription, open the basic SKU on Google Play.
                openPlayStoreSubscriptionsEvent.postValue(YEAR_SKU)
            }
            hasMonth && !hasSix && !hasYear -> {
                // If the user just has a premium subscription, downgrade.
                buy(sku = YEAR_SKU, oldSku = MONTH_SKU)
            }
            !hasMonth && hasSix && !hasYear -> {
                // If the user just has a premium subscription, downgrade.
                buy(sku = YEAR_SKU, oldSku = SIX_SKU)
            }
            else -> {
                // If the user dooes not have a subscription, buy the basic SKU.
                buy(sku = YEAR_SKU, oldSku = null)
            }
        }
    }

    /**
     * Upgrade to a premium subscription.
     */
    // fun buyUpgrade() = buy(Constants.PREMIUM_SKU, Constants.BASIC_SKU)

    /**
     * Use the Google Play Billing Library to make a purchase.
     */
    private fun buy(sku: String, oldSku: String?) {
        // First, determine whether the new SKU can be purchased.
        val isSkuOnDevice = deviceHasGooglePlaySubscription(purchases.value, sku)
        when {
            isSkuOnDevice -> {
                Log.e("Billing", "You cannot buy a SKU that is already owned: $sku. " +
                        "This is an error in the application trying to use Google Play Billing.")
                return
            }
        }

        // Second, determine whether the old SKU can be replaced.
        // If the old SKU cannot be used, set this value to null and ignore it.
        val oldSkuToBeReplaced = if (isOldSkuReplaceable(subscriptions.value, purchases.value, oldSku)) {
            oldSku
        } else {
            null
        }

        // Third, create the billing parameters for the purchase.
//        if (sku == oldSkuToBeReplaced) {
//            Log.i("Billing", "Re-subscribe.")
//        } else if (Constants.PREMIUM_SKU == sku && Constants.BASIC_SKU == oldSkuToBeReplaced) {
//            Log.i("Billing", "Upgrade!")
//        } else if (Constants.BASIC_SKU == sku && Constants.PREMIUM_SKU == oldSkuToBeReplaced) {
//            Log.i("Billing", "Downgrade...")
//        } else {
//            Log.i("Billing", "Regular purchase.")
//        }
        // Create the parameters for the purchase.
        val skuDetails = skusWithSkuDetails.value?.get(sku) ?: run {
            Log.e("Billing", "Could not find SkuDetails to make purchase.")
            return
        }
        val billingBuilder = BillingFlowParams.newBuilder().setSkuDetails(skuDetails)
        // Only set the old SKU parameter if the old SKU is already owned.
        if (oldSkuToBeReplaced != null && oldSkuToBeReplaced != sku) {
            purchaseForSku(purchases.value, oldSkuToBeReplaced)?.apply {
                billingBuilder.setSubscriptionUpdateParams(
                        BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                                .setOldSkuPurchaseToken(purchaseToken)
                                .build())
            }
        }
        val billingParams = billingBuilder.build()

        // Send the parameters to the Activity in order to launch the billing flow.
        buyEvent.postValue(billingParams)
    }

    /**
     * Determine if the old SKU can be replaced.
     */
    private fun isOldSkuReplaceable(
            subscriptions: List<SubscriptionStatus>?,
            purchases: List<Purchase>?,
            oldSku: String?
    ): Boolean {
        if (oldSku == null) return false
        val isOldSkuOnDevice = deviceHasGooglePlaySubscription(purchases, oldSku)
        return when {
            !isOldSkuOnDevice -> {
                Log.e("Billing", "You cannot replace a SKU that is NOT already owned: $oldSku. " +
                        "This is an error in the application trying to use Google Play Billing.")
                false
            }
            else -> {
                val subscription = subscriptionForSku(subscriptions, oldSku) ?: return false
                if (subscription.subAlreadyOwned) {
                    Log.i("Billing", "The old subscription is used by a " +
                            "different app account. However, it was paid for by the same " +
                            "Google account that is on this device.")
                    false
                } else {
                    true
                }
            }
        }
    }

}
