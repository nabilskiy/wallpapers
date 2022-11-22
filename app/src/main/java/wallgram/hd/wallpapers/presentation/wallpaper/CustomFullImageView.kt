package wallgram.hd.wallpapers.presentation.wallpaper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import javax.inject.Inject

@AndroidEntryPoint
class CustomFullImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), MyView {

//    override fun loadImage(preview: String, original: String) {
//
//        load(preview) {
//            // placeholderMemoryCacheKey(MemoryCache.Key(key = preview))
//            listener(onSuccess = { request, result ->
//                load(original) {
//                    placeholderMemoryCacheKey(MemoryCache.Key(key = preview))
//                }
//            })
//        }
//    }

    override fun loadImage(preview: String, original: String) {
        Glide.with(this).load(original)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(Glide.with(context).load(preview).centerCrop())
            .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#222222"))))
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(this)
    }

}
