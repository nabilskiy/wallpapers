package wallgram.hd.wallpapers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import wallgram.hd.wallpapers.data.gallery.GalleryCloud
import wallgram.hd.wallpapers.data.gallery.GalleryService
import wallgram.hd.wallpapers.model.ServerResponse

sealed class WallpaperRequest {

    open fun getTitle(): String = ""
    open fun query(): String = ""
    open fun needToLoadMore(): Boolean = true

    // open fun getSort(): String = ""
    open fun tabIndex(): Int = 0

    abstract suspend fun getRequest(
        service: GalleryService,
        page: Int
    ): ServerResponse<GalleryCloud.Base>

    open fun showProgress(): Boolean = true

    abstract fun copy(sort: String): WallpaperRequest

    @Parcelize
    data class CATEGORY(val id: Int, val name: String, val sort: String = "date") : WallpaperRequest(),
        Parcelable {
        override fun getTitle() = name

        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersFromCategory(id, sort, page)
        }

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    data class TAG(val id: Int, val name: String, val sort: String = "date") : WallpaperRequest(),
        Parcelable {
        override fun getTitle() = name

        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersFromTag(id, sort, page)
        }

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    data class COLOR(val id: Int, val name: String, val sort: String = "date") : WallpaperRequest(),
        Parcelable {
        override fun getTitle() = name
        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItemsFromColor(sort, page, r(), g(), b())
        }

        private fun r() = id shr 16 and 0xFF
        private fun g() = id shr 8 and 0xFF
        private fun b() = id shr 0 and 0xFF

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    class DATE : WallpaperRequest(), Parcelable {
        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("date", page)
        }

        override fun copy(sort: String) = this

    }

    @Parcelize
    class POPULAR : WallpaperRequest(), Parcelable {

        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("popular", page)
        }

        override fun copy(sort: String) = this
    }

    @Parcelize
    class TRANDS : WallpaperRequest(), Parcelable {
        override fun tabIndex() = 1
        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("popular", page, 26)
        }

        override fun copy(sort: String) = this
    }

    @Parcelize
    class RANDOM : WallpaperRequest(), Parcelable {

        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("random", page)
        }

        override fun copy(sort: String) = this

    }

    @Parcelize
    class SIMILAR(private val pic: Int) : WallpaperRequest(), Parcelable {
        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return service.getSimilarWallpapers(pic, page)
        }

        override fun copy(sort: String) = this

    }

    @Parcelize
    class SEARCH(private val query: String) : WallpaperRequest(), Parcelable {
        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> = service.search(query, page)

        override fun copy(sort: String) = this

        override fun query() = query
    }

    @Parcelize
    class FAVORITES: WallpaperRequest(), Parcelable{
        override suspend fun getRequest(
            service: GalleryService,
            page: Int
        ): ServerResponse<GalleryCloud.Base> {
            return ServerResponse(listOf())
        }

        override fun copy(sort: String) = this
        override fun showProgress() = false
        override fun needToLoadMore() = false
    }

}
