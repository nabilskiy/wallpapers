package wallgram.hd.wallpapers.presentation.search

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class SearchUi: ItemUi {

    override fun type() = 10

    override fun show(vararg views: MyView) = Unit

    override fun id() = "search"

    override fun content() = "search"


}