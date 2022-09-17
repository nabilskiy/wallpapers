package wallgram.hd.wallpapers.presentation.dialogs

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.work.WorkInfo
import com.google.android.material.snackbar.Snackbar
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.workers.WallpaperDownloader
import wallgram.hd.wallpapers.presentation.wallpaper.State
import wallgram.hd.wallpapers.util.dp
import wallgram.hd.wallpapers.util.getUri
import wallgram.hd.wallpapers.views.CustomPopupWindow
import java.io.File
import java.lang.ref.WeakReference
import java.util.logging.Handler

interface DownloadPopup {

    fun setState(state: State)
    fun hide()

    class Base(val root: View, val view: View) : DownloadPopup {
        private var window: CustomPopupWindow? = null

        init {
            val root = root
            val deviceWidth = root.width - 2 * 20.dp
            window = CustomPopupWindow(view).apply {
                width = deviceWidth
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                animationStyle = R.style.PopupTopAnimation
            }
        }

        private fun show() {
            window?.let { popupWindow ->
                if (!popupWindow.isShowing)
                    popupWindow.showAtLocation(
                        root, Gravity.BOTTOM, 0, 450
                    )
            }
        }

        private fun setMessage(message: CharSequence) = window?.let {
            it.contentView.findViewById<TextView>(R.id.text).apply {
                text = message
            }
        }

        private fun setIcon(resource: Int) = window?.let {
            it.contentView.findViewById<ImageButton>(R.id.action_btn).apply {
                isVisible = resource != 0
                setImageResource(resource)
            }
        }

        private fun setDuration(time: Long) = window?.setDuration(time)

        private fun setClickListener(clickListener: View.OnClickListener) =
            window?.let {
                it.contentView.findViewById<ImageButton>(R.id.action_btn).apply {
                    setOnClickListener(clickListener)
                }
            }


        override fun hide() {
            window?.dismiss()
        }

        override fun setState(state: State) = with(state) {
            setMessage(message())
            setIcon(icon())
            setDuration(duration())
            setClickListener { click() }

            show()
        }
    }

}
