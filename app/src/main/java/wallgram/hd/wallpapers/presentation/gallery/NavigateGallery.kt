package wallgram.hd.wallpapers.presentation.gallery

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.wallpapers.UpdateSave
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

interface NavigateGallery {

    fun navigate(item: Int)

    class Base @Inject constructor(
        private val communication: UpdateSave.Update
    ) : NavigateGallery {

        val modo = App.modo

        override fun navigate(item: Int) {
            //interactor.save(WallpaperRequest.DATE(), item)
            communication.map(true)
            modo.externalForward(Screens.Wallpaper())
        }
    }
}