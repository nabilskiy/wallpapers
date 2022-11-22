package wallgram.hd.wallpapers.presentation.subscribe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import org.joda.time.DateTime
import org.joda.time.Period
import wallgram.hd.wallpapers.YEAR_SKU
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.domain.home.HomeDomain

interface Subscription {

    fun <T> map(mapper: Mapper<T>): T

    class Empty : Subscription {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(
            "", "", canBuySku = false, purchased = false, 0L, ""
        )
    }

    data class Base(
        private val sku: String,
        private val price: String,
        private val canBuySku: Boolean,
        private val purchased: Boolean,
        private val date: Long,
        private val period: String
    ) : Subscription {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(sku, price, canBuySku, purchased, date, period)
    }

    interface Mapper<T> {
        fun map(
            sku: String,
            price: String,
            canBuySku: Boolean,
            purchased: Boolean,
            date: Long,
            period: String
        ): T

        class Purchased : Mapper<Boolean> {
            override fun map(
                sku: String,
                price: String,
                canBuySku: Boolean,
                purchased: Boolean,
                date: Long,
                period: String
            ) = purchased

        }

        class Ui : Mapper<Pair<String, Boolean>> {
            override fun map(
                sku: String,
                price: String,
                canBuySku: Boolean,
                purchased: Boolean,
                date: Long,
                period: String
            ) = Pair(price, purchased)

        }

        class Id : Mapper<String> {
            override fun map(
                sku: String,
                price: String,
                canBuySku: Boolean,
                purchased: Boolean,
                date: Long,
                period: String
            ) = sku
        }

        class Description : Mapper<Pair<String, String>> {
            override fun map(
                sku: String,
                price: String,
                canBuySku: Boolean,
                purchased: Boolean,
                date: Long,
                period: String
            ) = Pair(sku, price)
        }

        class Date : Mapper<DateTime> {
            override fun map(
                sku: String,
                price: String,
                canBuySku: Boolean,
                purchased: Boolean,
                date: Long,
                period: String
            ): DateTime {
                val formattedPeriod = Period.parse(period)
                return DateTime(date).plus(formattedPeriod)
            }
        }
    }

}

//sealed class Subscription() {
//
//    abstract fun sku(): String
//    abstract fun price(): String
//    abstract fun canBuySku(): Boolean
//    abstract fun isPurchased(): Boolean
//
////    class FREE(private val billingRepository: BillingRepository) : Subscription() {
////        override fun sku() = "default"
////        override fun price() =
////            billingRepository.getSkuCurrencyCode(YEAR_SKU).asLiveData()
////
////        override fun canBuySku() =
////            MutableLiveData<Boolean>().apply { postValue(false) }
////
////        override fun isPurchased() =
////            billingRepository.isFreePurchased().asLiveData()
////    }
//
//    class MONTH(private val price: String, private val canBuySku: Boolean, private val isPurchased: Boolean) : Subscription() {
//        override fun sku() = "1month"
//        override fun price() =
//            price
//
//        override fun canBuySku() =
//            canBuySku
//
//        override fun isPurchased() = isPurchased
//    }
//
//    class YEAR(private val billingRepository: BillingRepository) : Subscription() {
//        override fun sku() = "year"
//        override fun price() =
//            ""
//
//        override fun canBuySku() =
//            true
//
//        override fun isPurchased() = false
//
//    }
//
//}
