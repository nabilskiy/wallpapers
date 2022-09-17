package wallgram.hd.wallpapers.core.data.ads.rewarded

import com.google.android.gms.ads.rewarded.RewardItem
import wallgram.hd.wallpapers.core.data.ads.AdCoreInterstitialCallback

interface AdRewardedCallback : AdCoreInterstitialCallback {

    fun onUserEarnedReward(tag: String, rewardItem: RewardItem)

}