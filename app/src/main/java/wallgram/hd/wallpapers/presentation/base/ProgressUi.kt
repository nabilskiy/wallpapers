package wallgram.hd.wallpapers.presentation.base

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class ProgressUi: ItemUi {
    override fun type() = 3

    override fun show(vararg views: MyView) = Unit

    override fun id() = "progress"

    override fun content() = "progress"


}