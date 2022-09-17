package wallgram.hd.wallpapers.presentation.base

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryCache
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.util.modo.Modo
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

interface NavigateToolbar {

    fun navigate()

    class Base @Inject constructor() : NavigateToolbar {

        val modo = App.modo

        override fun navigate() {

        }
    }
}