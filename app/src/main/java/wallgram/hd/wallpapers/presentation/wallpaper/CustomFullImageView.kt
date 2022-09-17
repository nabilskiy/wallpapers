package wallgram.hd.wallpapers.presentation.wallpaper

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


class CustomFullImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr), MyView {

    override fun loadImage(preview: String, original: String) {
        Glide.with(context).load(original)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(Glide.with(context).load(preview).centerCrop())
            .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#222222"))))
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(this)
    }


}
