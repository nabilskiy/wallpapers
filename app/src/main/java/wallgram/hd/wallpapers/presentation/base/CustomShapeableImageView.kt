package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class CustomShapeableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr), MyView {

    override fun loadImage(url: String) {
        Glide.with(context).load(url)

            .apply(
                RequestOptions.bitmapTransform(
                    wallgram.hd.wallpapers.util.ColorFilterTransformation(
                        Color.argb(80, 0, 0, 0)
                    )
                )
            )
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
            .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#252831"))))
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(this)
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }


}

