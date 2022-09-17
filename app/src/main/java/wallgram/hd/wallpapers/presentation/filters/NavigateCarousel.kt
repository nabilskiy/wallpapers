package wallgram.hd.wallpapers.presentation.filters

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.util.modo.Modo
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

interface NavigateCarousel {

    fun navigate(item: Pair<Int, String>)

    class Base @Inject constructor() : NavigateCarousel {

        val modo = App.modo

        override fun navigate(item: Pair<Int, String>) {
            modo.forward(Screens.CategoriesList(WallpaperRequest.CATEGORY(item.first, item.second)))
        }
    }
}