package wallgram.hd.wallpapers.domain.home

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.SaveSelect
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain

interface HomeRepository : SaveSelect {
    suspend fun filters(): HomesDomain
    fun save(wallpaperRequest: WallpaperRequest, position: Int, filter: Int)
    fun read(): GalleriesDomain
    fun position(): Int
}