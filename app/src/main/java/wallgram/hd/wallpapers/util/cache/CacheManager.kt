package wallgram.hd.wallpapers.util.cache

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import wallgram.hd.wallpapers.model.Gallery

class CacheManager : ICacheManager {
    override var isRewardedAdShowed: Boolean = false
    override var isInterstitialAdShowed: Boolean = false
    override var wallpapersData: MutableLiveData<PagingData<Gallery>> =
        MutableLiveData<PagingData<Gallery>>()

    override var similarData: MutableLiveData<PagingData<Gallery>> =
        MutableLiveData<PagingData<Gallery>>()
}