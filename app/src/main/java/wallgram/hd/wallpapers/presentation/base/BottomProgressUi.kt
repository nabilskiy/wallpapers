package wallgram.hd.wallpapers.presentation.base

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class BottomProgressUi : ItemUi {
    override fun type() = 8

    override fun show(vararg views: MyView) = Unit

    override fun id() = "-8"

    override fun content() = "bottom_progress"


}