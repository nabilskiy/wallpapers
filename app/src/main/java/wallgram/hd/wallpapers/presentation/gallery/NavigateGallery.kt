package wallgram.hd.wallpapers.presentation.gallery

import coil.memory.MemoryCache
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.SaveSelect
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.wallpapers.UpdateSave
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

interface NavigateGallery {

    fun navigate(id: Int, requestId: String)

    class Base @Inject constructor(
        private val saveSelect: SaveSelect,
        private val communication: UpdateSave.Update
    ) : NavigateGallery {

        val modo = App.modo

        override fun navigate(id: Int, requestId: String) {
            saveSelect.save(id, requestId)
            modo.externalForward(Screens.Wallpaper())
        }
    }
}