package wallgram.hd.wallpapers.presentation.base

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class FullSizeErrorUi : ItemUi {
    override fun type() = 11

    override fun show(vararg views: MyView) = Unit

    override fun id() = "fullsize_error"

    override fun content() = "fullsize_error"


}