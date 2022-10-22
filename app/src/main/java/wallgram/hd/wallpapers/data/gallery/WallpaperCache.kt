package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.WallpaperRequest

interface WallpaperCache {

    fun id(): Int
    fun request(): WallpaperRequest

    data class Base(
        private val id: Int,
        private val request: WallpaperRequest
    ) : WallpaperCache {
        override fun id() = id
        override fun request() = request
    }

    class Empty : WallpaperCache {
        override fun request() = WallpaperRequest.DATE()
        override fun id() = 0
    }

}

