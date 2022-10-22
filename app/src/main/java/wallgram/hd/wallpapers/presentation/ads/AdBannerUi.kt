package wallgram.hd.wallpapers.presentation.ads

import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class AdBannerUi: ItemUi {
    override fun type() = ViewIds.AD_BANNER

    override fun show(vararg views: MyView) {

    }

    override fun id() = "banner"

    override fun content() = "banner"
}