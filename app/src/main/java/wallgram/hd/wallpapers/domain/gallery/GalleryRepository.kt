package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryData

interface GalleryRepository {

    suspend fun gallery(wallpaperRequest: WallpaperRequest): GalleriesDomain
   // suspend fun search(query: String): GalleriesDomain

    fun save(wallpaperRequest: WallpaperRequest, position: Int)
    fun update(wallpaperRequest: WallpaperRequest, position: Int)
    fun read(): Triple<GalleriesDomain, Int, WallpaperRequest>
    fun clear()

    fun getCachedData() : ArrayList<GalleryData>
}