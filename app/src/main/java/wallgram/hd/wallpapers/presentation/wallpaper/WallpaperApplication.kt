package wallgram.hd.wallpapers.presentation.wallpaper

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R

class WallpaperApplication {

    class Enqueued(private val onClick: () -> Unit) :
        State.Abstract(R.string.wallpaper_application) {
        override fun duration() = Snackbar.LENGTH_INDEFINITE
        override fun click() = onClick.invoke()
    }

    class Applied : State.Abstract(R.string.wallpaper_applied) {
        override fun icon() = 0
        override fun visible() = false
    }

    class Error : State.Abstract(R.string.wallpaper_apply_error) {
        override fun icon() = 0
        override fun visible() = false
    }

}