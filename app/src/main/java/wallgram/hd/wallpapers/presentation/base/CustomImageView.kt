package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import javax.inject.Inject

@AndroidEntryPoint
class CustomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), MyView {

    @Inject
    lateinit var displayProvider: DisplayProvider

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val size = displayProvider.getWallpaperHeight().y

        layoutParams.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = size
        }
    }


    override fun loadImage(preview: String, original: String) {
        Glide.with(context).load(preview)
            .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#222222"))))
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(this)
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}

