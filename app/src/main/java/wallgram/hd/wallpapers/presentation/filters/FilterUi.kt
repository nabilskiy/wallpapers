package wallgram.hd.wallpapers.presentation.filters

import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

interface FilterUi {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val id: Int,
        private val name: String,
        private val background: String,
        private val navigateFilter: NavigateFilter
    ) : FilterUi, ItemUi {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(id, name, background, listOf())
        override fun type(): Int = 1

        override fun show(vararg views: MyView) {
            views[0].loadImage(background)
            views[1].show(name)
            views[0].handleClick {

                navigateFilter.navigate(Pair(id, name))
            }
        }

        override fun id(): String = id.toString()

        override fun content(): String = name

        override fun getSpanSize(spanCount: Int, position: Int) = 1

    }

    interface Mapper<T> {
        fun map(id: Int, name: String, background: String, sample: List<GalleryDomain>): T

        class Display : Mapper<Pair<Int, String>> {
            override fun map(
                id: Int,
                name: String,
                background: String,
                sample: List<GalleryDomain>
            ) = Pair(id, name)
        }
    }
}