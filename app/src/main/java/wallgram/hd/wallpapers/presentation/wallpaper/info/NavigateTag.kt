package wallgram.hd.wallpapers.presentation.wallpaper.info

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.util.modo.Modo
import wallgram.hd.wallpapers.util.modo.forward
import wallgram.hd.wallpapers.util.modo.launch
import javax.inject.Inject

interface NavigateTag {

    fun navigate(item: Pair<Int, String>)
    fun navigate(url: String)

    class Base @Inject constructor() : NavigateTag {

        val modo = App.modo

        override fun navigate(item: Pair<Int, String>) {
            modo.forward(Screens.CategoriesList(WallpaperRequest.TAG(item.first, item.second)))
        }

        override fun navigate(url: String) {
            modo.launch(Screens.Browser(url))
        }
    }
}