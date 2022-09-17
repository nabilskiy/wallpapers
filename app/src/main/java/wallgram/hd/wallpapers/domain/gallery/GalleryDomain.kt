package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.favorites.IsFavorite
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.gallery.NavigateGallery
import javax.inject.Inject

interface GalleryDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val id: Int,
        private val width: Int,
        private val height: Int,
        private val preview: String,
        private val original: String,
        private val links: Links,
        private val filter: Int
    ) :
        GalleryDomain {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(id, width, height, preview, original, links, filter)
    }

    class Error(private val e: Exception) : GalleryDomain {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(e)
    }

    class Empty(
        private val id: Int,
        private val width: Int,
        private val height: Int,
        private val preview: String,
        private val original: String,
        private val links: Links,
        private val filter: Int
    ) : GalleryDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, width, height, preview, original, links, filter)
    }

    interface Mapper<T> {
        fun map(
            id: Int,
            width: Int,
            height: Int,
            preview: String,
            original: String,
            links: Links,
            filter: Int
        ): T

        fun map(e: Exception): T

        class Base @Inject constructor(
            private val cacheDataSource: FavoritesCacheDataSource,
            private val changeFavorite: ChangeFavorite
        ) :
            Mapper<GalleryUi> {

            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links, filter: Int
            ) = GalleryUi.Base(
                id,
                width,
                height,
                preview,
                original,
                links,
                filter,
                cacheDataSource.isFavorite(id),
                changeFavorite,
            )

            override fun map(e: Exception): GalleryUi = GalleryUi.Error(e)
        }
    }

}