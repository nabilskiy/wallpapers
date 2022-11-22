package wallgram.hd.wallpapers.presentation.ads

import com.google.android.gms.ads.AdView
import wallgram.hd.wallpapers.data.ads.recyclerbanner.RecyclerBannerAd
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class AdBannerUi(
    private val adBanner: RecyclerBannerAd
) : ItemUi {

    override fun type() = ViewIds.AD_BANNER

    override fun show(vararg views: MyView) {
        views[0].showAd(adBanner)
    }

    override fun id() = "-1"

    override fun content() = "banner"
}