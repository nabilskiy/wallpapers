package wallgram.hd.wallpapers.presentation.wallpaper

import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R

class Downloading {

    class Existent(title: Int, private val onClick: () -> Unit) :
        State.Abstract(title) {
        override fun click() = onClick.invoke()
        override fun icon() = R.drawable.ic_next
        override fun duration() = Snackbar.LENGTH_LONG
    }

    class Enqueued(private val onClick: () -> Unit) : State.Abstract(R.string.downloading) {
        override fun click() = onClick.invoke()
        override fun duration() = Snackbar.LENGTH_INDEFINITE
    }

    class Pending(private val onClick: () -> Unit) : State.Abstract(R.string.download_waiting) {
        override fun click() = onClick.invoke()
        override fun duration() = Snackbar.LENGTH_INDEFINITE
    }

    class Paused(private val onClick: () -> Unit) : State.Abstract(R.string.download_paused) {
        override fun click() = onClick.invoke()
        override fun duration() = Snackbar.LENGTH_INDEFINITE
    }

    class Cancelled() : State.Abstract(R.string.download_canceled) {
        override fun visible() = false
    }

    class Error : State.Abstract(R.string.download_error) {
        override fun icon() = 0
        override fun visible() = false
    }
}