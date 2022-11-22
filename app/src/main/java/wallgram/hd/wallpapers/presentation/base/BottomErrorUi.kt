package wallgram.hd.wallpapers.presentation.base

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class BottomErrorUi : ItemUi {
    override fun type() = 12

    override fun show(vararg views: MyView) = Unit

    override fun id() = "bottom_error"

    override fun content() = "bottom_error"


}