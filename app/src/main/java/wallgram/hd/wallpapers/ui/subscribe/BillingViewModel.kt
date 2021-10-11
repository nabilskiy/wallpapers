package wallgram.hd.wallpapers.ui.subscribe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.repository.billing.BillingRepository
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.util.SingleLiveEvent
import javax.inject.Inject

class BillingViewModel @Inject constructor(
    private val billingRepository: BillingRepository
): BaseViewModel() {

    val openPlayStoreSubscriptionsEvent = SingleLiveEvent<String>()

    companion object {
        val TAG = "TrivialDrive:" + BillingViewModel::class.java.simpleName
        private val skuToResourceIdMap: MutableMap<String, Int> = HashMap()

        init {

        }
    }

    class SkuDetails internal constructor(val sku: String, tdr: BillingRepository) {
        val title = tdr.getSkuTitle(sku).asLiveData()
        val description = tdr.getSkuDescription(sku).asLiveData()
        val price = tdr.getSkuPrice(sku).asLiveData()
//        val iconDrawableId = skuToResourceIdMap[sku]!!
    }

    fun getSkuDetails(sku: String): SkuDetails {
        return SkuDetails(sku, billingRepository)
    }

    fun canBuySku(sku: String): LiveData<Boolean> {
        return billingRepository.canPurchase(sku).asLiveData()
    }

    fun getCurrentSub(): LiveData<String>{
        return billingRepository.getCurrentSub().asLiveData()
    }

    fun isPurchased(sku: String): LiveData<Boolean> {
        return billingRepository.isPurchased(sku).asLiveData()
    }

    fun buySku(activity: Activity, sku: String, currentSku: String) {
        if(sku == currentSku){
            openPlayStoreSubscriptionsEvent.postValue(sku)
            return
        }
        billingRepository.buySku(activity, sku)
    }

    val billingFlowInProcess: LiveData<Boolean>
        get() = billingRepository.billingFlowInProcess.asLiveData()

    fun sendMessage(message: Int) {
        viewModelScope.launch {
            billingRepository.sendMessage(message)
        }
    }


}