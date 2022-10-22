package wallgram.hd.wallpapers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import wallgram.hd.wallpapers.data.gallery.GalleryCloud
import wallgram.hd.wallpapers.data.gallery.GalleryData
import wallgram.hd.wallpapers.data.gallery.GalleryService
import wallgram.hd.wallpapers.model.ServerResponse

sealed class WallpaperRequest {

    abstract fun itemId(): String

    abstract class Abstract(private val itemId: String) : WallpaperRequest() {

        private var page = 1
        private var data: MutableList<GalleryData> = ArrayList()

        override fun itemId() = itemId
        override fun page() = page
        override fun nextPage() {
            page += 1
        }

        override fun initialPage() {
            page = 1
            clear()
        }

        private fun clear(){
            data.clear()
        }

        override fun data() = data

        override fun setData(newData: List<GalleryData>) = this.apply {
            data = ArrayList(newData)
        }

        override fun updateData(newData: List<GalleryData>) = this.apply {
            data.clear()
            data.addAll(newData)
        }
    }

    open fun getTitle(): String = ""
    open fun query(): String = ""
    open fun needToLoadMore(): Boolean = true

    // open fun getSort(): String = ""
    open fun tabIndex(): Int = 0


    abstract fun page(): Int
    abstract fun nextPage()
    abstract fun initialPage()

    abstract fun data(): List<GalleryData>
    abstract fun updateData(newData: List<GalleryData>): WallpaperRequest
    abstract fun setData(newData: List<GalleryData>): WallpaperRequest


    abstract suspend fun getRequest(
        service: GalleryService
    ): ServerResponse<GalleryCloud.Base>

    open fun showProgress(): Boolean = true

    abstract fun copy(sort: String): WallpaperRequest

    @Parcelize
    data class CATEGORY(val id: Int, val name: String, val sort: String = "date") :
        Abstract("category_$sort:$id"),
        Parcelable {
        override fun getTitle() = name

        override suspend fun getRequest(
            service: GalleryService,
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersFromCategory(id, sort, page())
        }

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    data class Filter(val id: Int, val name: String, val sort: String = "date") :
        Abstract("filter_$id"),
        Parcelable {
        override fun getTitle() = name

        override suspend fun getRequest(
            service: GalleryService,
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersFromCategory(id, sort, page())
        }

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    data class TAG(val id: Int, val name: String, val sort: String = "date") :
        Abstract("tag_$sort:$id"),
        Parcelable {
        override fun getTitle() = name

        override suspend fun getRequest(
            service: GalleryService,
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersFromTag(id, sort, page())
        }

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    data class COLOR(val id: Int, val name: String, val sort: String = "date") :
        Abstract("color_$sort:$id"),
        Parcelable {
        override fun getTitle() = name
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItemsFromColor(sort, page(), r(), g(), b())
        }

        private fun r() = id shr 16 and 0xFF
        private fun g() = id shr 8 and 0xFF
        private fun b() = id shr 0 and 0xFF

        override fun copy(sort: String) = copy(id = id, name = name, sort = sort)
    }

    @Parcelize
    class DATE : Abstract("date"), Parcelable {
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("date", page())
        }

        override fun copy(sort: String) = this

    }

    @Parcelize
    class POPULAR : Abstract("popular"), Parcelable {

        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("popular", page())
        }

        override fun copy(sort: String) = this
    }

    @Parcelize
    class TRANDS : Abstract("trands"), Parcelable {
        override fun tabIndex() = 1
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("popular", page(), 26)
        }

        override fun copy(sort: String) = this
    }

    @Parcelize
    class RANDOM : Abstract("random"), Parcelable {

        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return service.getWallpapersItems("random", page())
        }

        override fun copy(sort: String) = this

    }

    @Parcelize
    class SIMILAR(private val id: Int) : Abstract("similar_$id"), Parcelable {
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return service.getSimilarWallpapers(id, page())
        }

        override fun copy(sort: String) = this

    }


    @Parcelize
    class SEARCH(private var query: String) : Abstract("search"), Parcelable {
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> = service.search(query(), page())

        override fun copy(sort: String) = this.apply {
            query = sort
        }

        override fun query() = query
    }

    @Parcelize
    class FAVORITES : Abstract("favorites"), Parcelable {
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return ServerResponse(listOf())
        }

        override fun copy(sort: String) = this
        override fun showProgress() = false
        override fun needToLoadMore() = false
    }

    @Parcelize
    class History : Abstract("history"), Parcelable {
        override suspend fun getRequest(
            service: GalleryService
        ): ServerResponse<GalleryCloud.Base> {
            return ServerResponse(listOf())
        }

        override fun copy(sort: String) = this
        override fun showProgress() = false
        override fun needToLoadMore() = false
    }

}
