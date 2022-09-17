package wallgram.hd.wallpapers.presentation.wallpaper

import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R

class Downloading {

    class Existent(private val title: String, private val onClick: () -> Unit): State.Abstract(title){
        override fun click() = onClick.invoke()
        override fun icon() = R.drawable.ic_next
    }


    class Enqueued(private val onClick: () -> Unit) : State.Abstract("Загрузка...") {
        override fun click() = onClick.invoke()
        override fun duration() = LENGTH_INDEFINITE
    }

    class Error : State.Abstract("Ошибка установки"){
        override fun icon() = 0
    }

}