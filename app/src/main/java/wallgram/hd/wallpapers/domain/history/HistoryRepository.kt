package wallgram.hd.wallpapers.domain.history

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain

interface HistoryRepository {

    suspend fun history(): GalleriesDomain

    fun save(wallpaperRequest: WallpaperRequest, position: Int)
    fun read(): GalleriesDomain
    fun position(): Int

}