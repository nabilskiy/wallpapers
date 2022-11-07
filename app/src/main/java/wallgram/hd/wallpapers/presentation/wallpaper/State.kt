package wallgram.hd.wallpapers.presentation.wallpaper

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R


interface State {

    fun message(): Int
    fun duration(): Int
    fun icon(): Int
    fun click()
    fun visible(): Boolean

    abstract class Abstract(@StringRes val message: Int) : State {

        override fun message() = message
        override fun icon() = R.drawable.ic_close_round
        override fun duration() = Snackbar.LENGTH_SHORT
        override fun click() = Unit
        override fun visible() = true
    }

}