package wallgram.hd.wallpapers.presentation.base.carousel

import wallgram.hd.wallpapers.presentation.base.adapter.EmptyAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.colors.ColorUi

class CarouselUi<T : ItemUi>(private val items: List<T>) : ItemUi {
    override fun type() = 4

    override fun show(vararg views: MyView) = Unit
    override fun showCarousel(
        genericAdapter: GenericAdapter<*>,
        vararg views: MyView
    ) {
        views[0].show(items, genericAdapter)
    }

    override fun id() = "carousel"

    override fun content() = "carousel"


}