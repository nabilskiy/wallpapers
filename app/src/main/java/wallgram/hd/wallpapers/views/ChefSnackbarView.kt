package wallgram.hd.wallpapers.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.material.snackbar.ContentViewCallback
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.wallpaper.State

class ChefSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    init {
        View.inflate(context, R.layout.view_loading, this)
        clipToPadding = false

    }

    fun update(state: State) = with(state) {
        setMessage(message())
        setIcon(icon())
        setIconVisible(visible())
        setClickListener { click() }
    }

    private fun setMessage(message: Int) {
        findViewById<TextView>(R.id.text).setText(message)
    }

    private fun setIconVisible(visible: Boolean){
        findViewById<ImageButton>(R.id.action_btn).isVisible = visible
    }

    private fun setIcon(resource: Int) {
        findViewById<ImageButton>(R.id.action_btn).apply {
            isVisible = resource != 0
            setImageResource(resource)
        }
    }

    private fun setClickListener(clickListener: View.OnClickListener) =
        findViewById<ImageButton>(R.id.action_btn).apply {
            setOnClickListener(clickListener)
        }

    override fun animateContentIn(delay: Int, duration: Int) {

    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}