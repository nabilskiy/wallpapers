package wallgram.hd.wallpapers.data.billing

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import wallgram.hd.wallpapers.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.MONTH_SKU
import wallgram.hd.wallpapers.YEAR_SKU
import wallgram.hd.wallpapers.presentation.subscribe.ChangeSubscription
import wallgram.hd.wallpapers.presentation.subscribe.Subscription
import wallgram.hd.wallpapers.presentation.subscribe.SubscriptionsDomain


class BillingRepository constructor(
    private val billingDataSource: BillingDataSource,
    private val defaultScope: CoroutineScope,
    private val changeSubscription: ChangeSubscription
) {

    private fun postMessagesFromBillingFlow() {
        defaultScope.launch {
            try {
                billingDataSource.getNewPurchases().collect { skuList ->
                    // TODO: Handle multi-line purchases better
                    for (sku in skuList) {
                        when (sku) {
                            SKU_INFINITE_GAS_MONTHLY,
                            SKU_INFINITE_GAS_YEARLY -> {
                                // this makes sure that upgrades/downgrades to subscriptions are
                                // reflected correctly in our user interface
                                billingDataSource.refreshPurchases()
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.d(TAG, "Collection complete")
            }
            Log.d(TAG, "Collection Coroutine Scope Exited")
        }
    }

    fun buySku(activity: Activity, sku: String) {
        var oldSku: String? = null
        when (sku) {
            SKU_INFINITE_GAS_MONTHLY -> oldSku = SKU_INFINITE_GAS_YEARLY
            SKU_INFINITE_GAS_YEARLY -> oldSku = SKU_INFINITE_GAS_MONTHLY
        }
        if (oldSku == null) {
            billingDataSource.launchBillingFlow(activity, sku)
        } else {
            billingDataSource.launchBillingFlow(activity, sku, oldSku)
        }
    }

    /**
     * Return Flow that indicates whether the sku is currently purchased.
     *
     * @param sku the SKU to get and observe the value for
     * @return Flow that returns true if the sku is purchased.
     */
    fun isPurchased(sku: String): Flow<Boolean> {
        return billingDataSource.isPurchased(sku)
    }

    fun canPurchase(sku: String): Flow<Boolean> {
        return billingDataSource.canPurchase(sku)
    }

    fun getSubscriptions(): LiveData<SubscriptionsDomain> {
        val month = billingDataSource.getSubscription(SKU_INFINITE_GAS_MONTHLY)
        val year = billingDataSource.getSubscription(SKU_INFINITE_GAS_YEARLY)
        return combine(month, year) { _month, _year ->
            if(_month is Subscription.Empty && _year is Subscription.Empty)
                SubscriptionsDomain.Empty()
            else SubscriptionsDomain.Base(listOf(_month, _year))
        }.asLiveData()
    }

    private fun isSubscribed(): Flow<Boolean> {
        val monthlySubPurchasedFlow = isPurchased(SKU_INFINITE_GAS_MONTHLY)
        val yearlySubPurchasedFlow = isPurchased(SKU_INFINITE_GAS_YEARLY)
        return combine(
            monthlySubPurchasedFlow,
            yearlySubPurchasedFlow
        ) { monthlySubPurchased, yearlySubPurchased ->
            when {
                monthlySubPurchased || yearlySubPurchased -> true
                else -> false
            }
        }
    }

    companion object {

        const val SKU_INFINITE_GAS_MONTHLY = MONTH_SKU
        const val SKU_INFINITE_GAS_YEARLY = YEAR_SKU
        val TAG = BillingRepository::class.simpleName
        val SUBSCRIPTION_SKUS = arrayOf(
            SKU_INFINITE_GAS_MONTHLY,
            SKU_INFINITE_GAS_YEARLY
        )
    }

    init {
        postMessagesFromBillingFlow()

        // Since both are tied to application lifecycle, we can launch this scope to collect
        // consumed purchases from the billing data source while the app process is alive.
        defaultScope.launch {
            isSubscribed().collect {
                Log.d("SUB_GET", it.toString())
                withContext(Dispatchers.Main) {
                    changeSubscription.changeSubscription(it)
                }

            }

        }
    }
}
