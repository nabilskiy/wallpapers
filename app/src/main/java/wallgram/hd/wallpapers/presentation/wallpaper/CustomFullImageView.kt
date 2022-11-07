package wallgram.hd.wallpapers.presentation.wallpaper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.memory.MemoryCache
import coil.util.CoilUtils
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import javax.inject.Inject

@AndroidEntryPoint
class CustomFullImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), MyView {

    override fun loadImage(preview: String, original: String) {

        load(preview) {
           // placeholderMemoryCacheKey(MemoryCache.Key(key = preview))
            listener(onSuccess = { request, result ->
                load(original) {
                    placeholderMemoryCacheKey(MemoryCache.Key(key = preview))
                }
            })
        }
    }

}
