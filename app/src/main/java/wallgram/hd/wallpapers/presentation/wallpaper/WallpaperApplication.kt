package wallgram.hd.wallpapers.presentation.wallpaper

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R

class WallpaperApplication {

    class Enqueued(private val onClick: () -> Unit) : State.Abstract("Применение обоев...") {
        override fun duration() = LENGTH_INDEFINITE
        override fun click() = onClick.invoke()
    }

    class Applied : State.Abstract("Обои установлены"){
        override fun icon() = 0
    }
    class Error : State.Abstract("Ошибка установки"){
        override fun icon() = 0
    }

}