package wallgram.hd.wallpapers.presentation.favorite

import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class FavoritesEmptyUi : ItemUi {

    override fun type() = ViewIds.FAVORITES_EMPTY

    override fun show(vararg views: MyView) = Unit

    override fun id() = "search_empty"

    override fun content() = "search_empty"


}