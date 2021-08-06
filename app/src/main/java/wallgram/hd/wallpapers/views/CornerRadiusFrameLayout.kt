package wallgram.hd.wallpapers.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout
import wallgram.hd.wallpapers.util.dp

internal class CornerRadiusFrameLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Variables
    private var noCornerRadius = true
    private val path = Path()
    private val rect = RectF()
    private val outerRadii = floatArrayOf(
            // Top left corner
            0f, 0f,
            // Top right corner
            0f, 0f,
            // Bottom right corner
            0f, 0f,
            // Bottom left corner
            0f, 0f
    )

    init {
        initView()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rect.set(0f, 0f, w.toFloat(), h.toFloat())
        resetPath()
    }

    override fun draw(canvas: Canvas?) = when {
        noCornerRadius -> super.draw(canvas)

        else -> with(canvas!!) {
            val save = save()
            clipPath(path)

            super.draw(this)
            restoreToCount(save)
        }
    }

    //region PUBLIC METHODS

    internal fun setCornerRadius(radius: Float) {
        // Top left corner
        outerRadii[0] = radius
        outerRadii[1] = radius

        // Top right corner
        outerRadii[2] = radius
        outerRadii[3] = radius

        noCornerRadius = (radius == 0f)

        // Discard invalid events
        if (width == 0 || height == 0) return

        resetPath()
        invalidate()
    }

    private fun initView() {
        orientation = VERTICAL
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setCornerRadius(16.dp.toFloat())
    }

    private fun resetPath() = path.run {
        reset()
        addRoundRect(rect, outerRadii, Path.Direction.CW)
        close()
    }
}