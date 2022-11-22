package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.button.MaterialButton
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class CustomMaterialButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr), MyView {

    override fun handleClick(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}