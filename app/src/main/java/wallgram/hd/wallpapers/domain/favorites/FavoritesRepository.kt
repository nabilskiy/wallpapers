package wallgram.hd.wallpapers.domain.favorites

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain

interface FavoritesRepository {

    suspend fun favorites(): GalleriesDomain
    suspend fun history(): GalleriesDomain

    fun save(wallpaperRequest: WallpaperRequest, position: Int)
    fun read(): GalleriesDomain

}