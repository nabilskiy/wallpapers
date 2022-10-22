package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import javax.inject.Inject

interface GalleryData {

    fun <T> map(mapper: Mapper<T>): T
    fun <T> map(mapper: Mapper<T>, filter: Int = 0, requestId: String = ""): T

    class Base(
        private val id: Int,
        private val width: Int,
        private val height: Int,
        private val preview: String,
        private val original: String,
        private val links: Links
    ) : GalleryData {

        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(id, width, height, preview, original, links)

        override fun <T> map(mapper: Mapper<T>, filter: Int, requestId: String) =
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
            filter: Int = 0,
            requestId: String = ""
        ): T

        class Base @Inject constructor() :
            Mapper<GalleryDomain> {

            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links,
                filter: Int,
                requestId: String
            ) =
                GalleryDomain.Base(id, width, height, preview, original, links, filter, requestId)

        }

        class Id(private val data: Int): Mapper<Boolean>{
            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String,
                links: Links,
                filter: Int,
                requestId: String
            ) = data == id

        }
    }

}