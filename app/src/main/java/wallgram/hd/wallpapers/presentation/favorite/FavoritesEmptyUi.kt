package wallgram.hd.wallpapers.presentation.favorite

import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class FavoritesEmptyUi(
    private val text: Int,
    private val icon: Int
) : ItemUi {

    override fun type() = ViewIds.FAVORITES_EMPTY

    override fun show(vararg views: MyView) {
        views[0].show(text)
        views[0].showImageResource(icon)
    }

    override fun id() = "" + text

    override fun content() = "" + icon


}