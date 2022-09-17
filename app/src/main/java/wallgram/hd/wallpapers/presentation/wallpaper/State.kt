package wallgram.hd.wallpapers.presentation.wallpaper

import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R

const val LENGTH_NORMAL = 3000L
const val LENGTH_INDEFINITE = -1L

interface State {

    fun message(): String
    fun duration(): Long
    fun icon(): Int
    fun click()

    abstract class Abstract(val message: String) : State {

        override fun message() = message
        override fun icon() = R.drawable.ic_close_round
        override fun duration() = LENGTH_NORMAL
        override fun click() = Unit
    }

}