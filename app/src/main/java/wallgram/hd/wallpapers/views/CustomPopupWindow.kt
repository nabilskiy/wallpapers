package wallgram.hd.wallpapers.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupWindow
import wallgram.hd.wallpapers.util.modo.Pop
import java.time.Duration


class CustomPopupWindow(view: View): PopupWindow(view), PopupWindow.OnDismissListener {

    private var closeDelayTime = 3000L
    private var handler: Handler? = null
   // private var onDismissListener: OnDismissListener

    fun setDuration(duration: Long){
        closeDelayTime = duration
        onShow()
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        onShow()
        super.showAtLocation(parent, gravity, x, y)
    }

    private fun onShow(){
        schedule()
    }

    private fun schedule(){
        if(closeDelayTime > 0){
            if(handler == null)
                handler = Handler(Looper.getMainLooper())
            handler!!.removeCallbacks(closeRunnable)
            handler!!.postDelayed(closeRunnable, closeDelayTime)
        }
    }

    override fun onDismiss() {
        handler?.removeCallbacks(closeRunnable)

    }

    private val closeRunnable = Runnable {
        dismiss()
    }
}