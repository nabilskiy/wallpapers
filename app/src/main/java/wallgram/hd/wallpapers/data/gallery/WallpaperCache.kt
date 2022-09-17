package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.WallpaperRequest

interface WallpaperCache {

    fun list(): List<GalleryData>
    fun position(): Int
    fun wallpaperRequest(): WallpaperRequest

    data class Base(
        val list: List<GalleryData>,
        val position: Int,
        val wallpaperRequest: WallpaperRequest
    ) : WallpaperCache {
        override fun list() = list
        override fun position() = position
        override fun wallpaperRequest() = wallpaperRequest
    }

    class Empty : WallpaperCache {
        override fun list() = listOf<GalleryData>()
        override fun position() = 0
        override fun wallpaperRequest() = WallpaperRequest.DATE()
    }

}

