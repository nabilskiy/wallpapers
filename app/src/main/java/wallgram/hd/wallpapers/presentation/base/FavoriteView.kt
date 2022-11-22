package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class FavoriteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr), MyView {

    override fun check(checked: Boolean) {
        setImageResource(if (checked) R.drawable.ic_favorite_filled else R.drawable.ic_not_favorite)
        super.check(checked)
    }

}