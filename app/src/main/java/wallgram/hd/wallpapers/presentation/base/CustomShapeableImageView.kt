package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.imageview.ShapeableImageView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.presentation.base.views.coil.transform.ColorFilterTransformation
import wallgram.hd.wallpapers.util.dp

class CustomShapeableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), MyView {

    override fun loadImage(url: String) {
        load(url) {
            placeholder(ColorDrawable(Color.parseColor("#222222")))
            transformations(
                RoundedCornersTransformation(4f.dp),
                ColorFilterTransformation(0xC1C1C1)
            )
        }
    }

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}

