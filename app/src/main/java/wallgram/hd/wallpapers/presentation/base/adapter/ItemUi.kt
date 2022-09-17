package wallgram.hd.wallpapers.presentation.base.adapter

import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.presentation.colors.ColorUi

interface ItemUi {

    fun type(): Int

    fun show(vararg views: MyView)

    fun showCarousel(genericAdapter: GenericAdapter<*>, vararg views: MyView) = Unit

    fun id(): String

    fun filter(): Int = 0

    fun content(): String

    fun uri(): Pair<String, String> = Pair("", "")

    fun changeFavorite() = Unit
    fun changeHistory() = Unit

    fun isFavorite(): Boolean = false

    fun getSpanSize(spanCount: Int, position: Int): Int = spanCount
}