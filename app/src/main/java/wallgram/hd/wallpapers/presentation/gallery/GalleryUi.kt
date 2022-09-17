package wallgram.hd.wallpapers.presentation.gallery

import android.util.Log
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.filters.FilterUi

interface GalleryUi: ItemUi {

    fun <T> map(mapper: Mapper<T>): T

    class Error(private val e: Exception): ItemUi, GalleryUi{
        override fun type(): Int {
            return 12
        }

        override fun show(vararg views: MyView) {

        }

        override fun id() = "error"

        override fun content() = "error"
        override fun <T> map(mapper: Mapper<T>) = mapper.map(e)

    }

    class Base(
        private val id: Int,
        private val width: Int,
        private val height: Int,
        private val preview: String,
        private val original: String,
        private val links: Links,
        private val filter: Int,
        private val isFavorite: Boolean,
        private val changeFavorite: ChangeFavorite
    ) : ItemUi, GalleryUi {

        override fun getSpanSize(spanCount: Int, position: Int) = 1

        override fun type(): Int = 6

        override fun show(vararg views: MyView) {
            views[0].loadImage(preview, original)
        }

        override fun id(): String = id.toString()
        override fun filter() = filter

        override fun content(): String = id.toString() + isFavorite

        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, width, height, preview, original, links)

        override fun changeFavorite() {
            changeFavorite.changeFavorite(map(Mapper.Favorite()))
        }

        override fun changeHistory() {
            changeFavorite.changeFavorite(map(Mapper.History()))
        }

        override fun uri() = Pair(preview, original)
        override fun isFavorite() = isFavorite
    }

    interface Mapper<T> {
        fun map(
            id: Int,
            width: Int,
            height: Int,
            preview: String,
            original: String,
            links: Links
        ): T
        fun map(e: Exception): T

        class Favorite : Mapper<GalleryCache.Base> {
            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links
            ): GalleryCache.Base =
                GalleryCache.Base(id, width, height, preview, original, 0, width, height, links, false)

            override fun map(e: Exception) = GalleryCache.Base(-1, 0, 0, "", "", 0, 0, 0, Links("", "", ""), false)

        }

        class History: Mapper<GalleryCache.Base>{
            override fun map(e: Exception) = GalleryCache.Base(-1, 0, 0, "", "", 0, 0, 0, Links("", "", ""), true)


            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links
            ): GalleryCache.Base =
                GalleryCache.Base(id, width, height, preview, original, 0, width, height, links, true)

        }

    }
}