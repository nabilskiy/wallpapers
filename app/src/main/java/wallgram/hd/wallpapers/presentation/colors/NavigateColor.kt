package wallgram.hd.wallpapers.presentation.colors

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.util.modo.Modo
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

interface NavigateColor {

    fun navigate(item: Pair<Int, String>)

    class Base @Inject constructor() : NavigateColor {

        val modo = App.modo

        override fun navigate(item: Pair<Int, String>) {
            modo.forward(Screens.CategoriesList(WallpaperRequest.COLOR(item.first, item.second)))
        }
    }
}