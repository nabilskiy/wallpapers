package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryData
import wallgram.hd.wallpapers.data.gallery.SaveSelect
import wallgram.hd.wallpapers.domain.home.HomesDomain

interface GalleryRepository : SaveSelect {

    suspend fun filters(): HomesDomain
    suspend fun gallery(request: WallpaperRequest): GalleriesDomain
    suspend fun favorites(): GalleriesDomain
    suspend fun history(): GalleriesDomain

    // suspend fun search(query: String): GalleriesDomain

    fun read(): Triple<GalleriesDomain, Int, WallpaperRequest>
    fun clear()
    fun clearAll()
    fun clear(request: WallpaperRequest)

    fun getCachedData(id: String): List<GalleryData>
}