package wallgram.hd.wallpapers.presentation.subscribe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import wallgram.hd.wallpapers.YEAR_SKU
import wallgram.hd.wallpapers.data.billing.BillingRepository

sealed class Subscription() {

    abstract fun sku(): String
    abstract fun price(): LiveData<String>
    abstract fun canBuySku(): LiveData<Boolean>
    abstract fun isPurchased(): LiveData<Boolean>

    class FREE(private val billingRepository: BillingRepository) : Subscription() {
        override fun sku() = "default"
        override fun price() =
            billingRepository.getSkuCurrencyCode(YEAR_SKU).asLiveData()

        override fun canBuySku() =
            MutableLiveData<Boolean>().apply { postValue(false) }

        override fun isPurchased() =
            billingRepository.isFreePurchased().asLiveData()
    }

    class MONTH(private val billingRepository: BillingRepository) : Subscription() {
        override fun sku() = "1month"
        override fun price() =
            billingRepository.getSkuPrice(sku()).asLiveData()

        override fun canBuySku() =
            billingRepository.canPurchase(sku()).asLiveData()

        override fun isPurchased() = billingRepository.isPurchased(sku()).asLiveData()
    }

    class YEAR(private val billingRepository: BillingRepository) : Subscription() {
        override fun sku() = "year"
        override fun price() =
            billingRepository.getSkuPrice(sku()).asLiveData()

        override fun canBuySku() =
            billingRepository.canPurchase(sku()).asLiveData()

        override fun isPurchased() = billingRepository.isPurchased(sku()).asLiveData()

    }

}
