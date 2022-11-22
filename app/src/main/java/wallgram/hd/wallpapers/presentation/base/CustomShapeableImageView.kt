package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.util.dp

class CustomShapeableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), MyView {

    override fun loadImage(url: String) {
        Glide.with(context).load(url)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
            .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#252831"))))
            .transform(CenterCrop(), RoundedCorners(4.dp))
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(this)
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}

