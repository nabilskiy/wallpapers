package wallgram.hd.wallpapers.presentation.home.scroll

import android.util.Log
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.presentation.base.CustomRecyclerView
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.filters.NavigateCarousel
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi

interface HomeCarouselUi {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val id: Int,
        private val name: String,
        private val items: List<GalleryUi>,
        private val navigateCarousel: NavigateCarousel
    ) : ItemUi, HomeCarouselUi {

        override fun <T> map(mapper: Mapper<T>) = mapper.map(id, name, items)

        override fun type(): Int = 7

        override fun show(vararg views: MyView) {
            views[0].show(name)
            views[0].handleClick {
                navigateCarousel.navigate(Pair(id, name))
            }
        }

        override fun showCarousel(
            genericAdapter: GenericAdapter<*>,
            vararg views: MyView
        ) {
            views[0].show(
                items,
                genericAdapter
            )
        }

        override fun id(): String = id.toString()

        override fun content(): String = name
    }

    interface Mapper<T> {
        fun map(
            id: Int,
            name: String,
            items: List<GalleryUi>
        ): T

        class Display : Mapper<Pair<Int, String>> {
            override fun map(
                id: Int,
                name: String,
                items: List<GalleryUi>
            ) = Pair(id, name)
        }
    }

}

