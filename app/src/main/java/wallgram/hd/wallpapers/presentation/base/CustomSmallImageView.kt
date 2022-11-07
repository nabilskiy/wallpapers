package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.util.dp
import javax.inject.Inject

@AndroidEntryPoint
class CustomSmallImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), MyView {

    @Inject
    lateinit var displayProvider: DisplayProvider

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val screenHeight = displayProvider.getScreenSize().y
        val wallpaperHeight = displayProvider.getWallpaperHeight().y
        val h = if (screenHeight > 1920) (wallpaperHeight / 1.2).toInt() else wallpaperHeight
        val screenWidth = displayProvider.getScreenSize().x

        val itemWidth = (screenWidth - 16.dp) / 3.33

        layoutParams.apply {
            width = itemWidth.toInt()
            height = h
        }
    }

    override fun loadImage(preview: String, original: String) {
        load(preview) {
            //memoryCacheKey(preview)
            placeholder(ColorDrawable(Color.parseColor("#222222")))
            transformations(RoundedCornersTransformation(4f.dp))
        }
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }


}

