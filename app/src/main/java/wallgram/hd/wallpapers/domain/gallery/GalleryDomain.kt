package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
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
        private val filter: Int,
        private val requestId: String = ""
    ) :
        GalleryDomain {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(id, width, height, preview, original, links, filter, requestId)
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
        private val filter: Int,
        private val requestId: String
    ) : GalleryDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, width, height, preview, original, links, filter, requestId)
    }

    interface Mapper<T> {
        fun map(
            id: Int,
            width: Int,
            height: Int,
            preview: String,
            original: String,
            links: Links,
            filter: Int,
            requestId: String
        ): T

        fun map(e: Exception): T

        class Base @Inject constructor(
            private val cacheDataSource: FavoritesCacheDataSource,
            private val changeFavorite: ChangeFavorite,
            private val navigateGallery: NavigateGallery
        ) :
            Mapper<GalleryUi> {

            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links, filter: Int,
                requestId: String
            ) = GalleryUi.Base(
                id,
                width,
                height,
                preview,
                original,
                links,
                filter,
                requestId,
                cacheDataSource.isFavorite(id),
                changeFavorite,
                navigateGallery,
            )

            override fun map(e: Exception): GalleryUi = GalleryUi.Error(e)
        }

        class Id : Mapper<Int> {
            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links,
                filter: Int,
                requestId: String
            ) = id

            override fun map(e: Exception) = 0

        }

        class Link: Mapper<String>{
            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links,
                filter: Int,
                requestId: String
            ) = original

            override fun map(e: Exception) = ""

        }

        class CompareId(private val id: Int) : Mapper<Boolean> {
            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links,
                filter: Int,
                requestId: String
            ) = id == this.id

            override fun map(e: Exception) = false

        }
    }

}