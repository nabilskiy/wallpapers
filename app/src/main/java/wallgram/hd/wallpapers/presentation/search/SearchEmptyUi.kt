package wallgram.hd.wallpapers.presentation.search

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class SearchEmptyUi: ItemUi {

    override fun type() = 11

    override fun show(vararg views: MyView) = Unit

    override fun id() = "search_empty"

    override fun content() = "search_empty"


}