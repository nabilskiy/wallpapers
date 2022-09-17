package wallgram.hd.wallpapers.presentation.colors

import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.filters.FilterUi

interface ColorUi {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val id: Int,
        private val title: String,
        private val color: Int,
        private val textColor: Int,
        private val startColor: Int,
        private val endColor: Int,
        private val value: Int,
        private val navigateColor: NavigateColor
    ) : ColorUi, ItemUi {
        override fun type() = 5

        override fun show(vararg views: MyView) = with(views[0]) {
            show(title)
            setGradientDrawable(startColor, endColor)
            textColor(textColor)
            handleClick{
                navigateColor.navigate(Pair(value, title))
            }
        }

        override fun id() = id.toString()

        override fun content() = title.toString()
        override fun <T> map(mapper: Mapper<T>) = mapper.map(id, title, color, textColor, startColor, endColor)

    }

    interface Mapper<T> {
        fun map(
            id: Int,
            title: String,
            color: Int,
            textColor: Int,
            startColor: Int,
            endColor: Int,
        ): T


    }

}

