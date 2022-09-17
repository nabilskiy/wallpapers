package wallgram.hd.wallpapers.data.billing

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import wallgram.hd.wallpapers.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.MONTH_SKU
import wallgram.hd.wallpapers.YEAR_SKU


class BillingRepository constructor(
    private val billingDataSource: BillingDataSource,
    private val defaultScope: CoroutineScope
) {
    private val gameMessages: MutableSharedFlow<Int> = MutableSharedFlow()

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
                                gameMessages.emit(R.string.app_name)
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

    suspend fun drive() {
//        when (val gasTankLevel = gasTankLevel().first()) {
//            GAS_TANK_INFINITE -> sendMessage(R.string.app_name)
//            GAS_TANK_MIN -> sendMessage(R.string.app_name)
//            else -> {
//                val newGasLevel = gasTankLevel - gameStateModel.decrementGas(GAS_TANK_MIN)
//                Log.d(TAG, "Old Gas Level: $gasTankLevel New Gas Level: $newGasLevel")
//                if (newGasLevel == GAS_TANK_MIN) {
//                    sendMessage(R.string.app_name)
//                } else {
//                    sendMessage(R.string.app_name)
//                }
        //         }
        //     }
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

    /**
     * We can buy gas if:
     * 1) We can add at least one unit of gas
     * 2) The billing data source allows us to purchase, which means that the item isn't already
     *    purchased.
     * For other skus, we rely on just the data from the billing data source. For subscriptions,
     * only one can be held at a time, and purchasing one subscription will use the billing feature
     * to upgrade or downgrade the user from the other.
     *
     * @param sku the SKU to get and observe the value for
     * @return Flow<Boolean> that returns true if the sku can be purchased
     */
    fun canPurchase(sku: String): Flow<Boolean> {
        return billingDataSource.canPurchase(sku)
    }

    /**
     * Combine the results from our subscription flow with our gas tank level from the game state
     * database to get our displayed gas tank level, which will be infinite if a subscription is
     * active.
     *
     * @return Flow that represents the gasTankLevel by game logic.
     */
    fun getCurrentSub(): Flow<String> {
        // val gasTankLevelFlow = gameStateModel.gasTankLevel()
        val monthlySubPurchasedFlow = isPurchased(SKU_INFINITE_GAS_MONTHLY)
        val yearlySubPurchasedFlow = isPurchased(SKU_INFINITE_GAS_YEARLY)
        return combine(
            monthlySubPurchasedFlow,
            yearlySubPurchasedFlow
        ) { monthlySubPurchased, yearlySubPurchased ->
            when {
                monthlySubPurchased -> MONTH_SKU
                yearlySubPurchased -> YEAR_SKU
                else -> DEFAULT_SKU
            }
        }
    }

    fun isFreePurchased(): Flow<Boolean> {
        // val gasTankLevelFlow = gameStateModel.gasTankLevel()
        val monthlySubPurchasedFlow = isPurchased(SKU_INFINITE_GAS_MONTHLY)
        val yearlySubPurchasedFlow = isPurchased(SKU_INFINITE_GAS_YEARLY)
        return combine(
            monthlySubPurchasedFlow,
            yearlySubPurchasedFlow
        ) { monthlySubPurchased, yearlySubPurchased ->
            when {
                monthlySubPurchased -> false
                yearlySubPurchased -> false
                else -> true
            }
        }
    }

    suspend fun isSubscribe(): Boolean = withContext(Dispatchers.IO) {
        when (getCurrentSub().first()) {
            MONTH_SKU, YEAR_SKU -> true
            else -> false
        }
    }

    suspend fun refreshPurchases() {
        billingDataSource.refreshPurchases()
    }

    val billingLifecycleObserver: LifecycleObserver
        get() = billingDataSource

    // There's lots of information in SkuDetails, but our app only needs a few things, since our
    // goods never go on sale, have introductory pricing, etc.
    fun getSkuTitle(sku: String): Flow<String> {
        return billingDataSource.getSkuTitle(sku)
    }



    fun getSkuCurrencyCode(sku: String): Flow<String> {
        return billingDataSource.getSkuCurrencyCode(sku)
    }

    fun getSkuPrice(sku: String): Flow<String> {
        return billingDataSource.getSkuPrice(sku)
    }

    fun getSkuDescription(sku: String): Flow<String> {
        return billingDataSource.getSkuDescription(sku)
    }

    val messages: Flow<Int>
        get() = gameMessages

    suspend fun sendMessage(stringId: Int) {
        gameMessages.emit(stringId)
    }

    val billingFlowInProcess: Flow<Boolean>
        get() = billingDataSource.getBillingFlowInProcess()


    companion object {
        // Source for all constants
        const val GAS_TANK_MIN = 0
        const val GAS_TANK_MAX = 4
        const val GAS_TANK_INFINITE = 5


        // SKU for subscription purchases (infinite gas)
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
            getCurrentSub().collect {
                Log.d("SUB_GET", it)

            }
        }
    }
}
