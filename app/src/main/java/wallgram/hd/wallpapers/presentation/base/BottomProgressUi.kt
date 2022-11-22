package wallgram.hd.wallpapers.presentation.base

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class BottomProgressUi : ItemUi {
    override fun type() = 8

    override fun show(vararg views: MyView) = Unit

    override fun id() = "bottom_progress"

    override fun content() = "bottom_progress"


}