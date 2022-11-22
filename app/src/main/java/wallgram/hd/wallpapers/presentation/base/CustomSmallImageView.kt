package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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
        Glide.with(context).load(preview)
            .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#222222"))))
            .transform(CenterCrop(), RoundedCorners(4.dp))
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(this)
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }


}

