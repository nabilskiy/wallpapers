package wallgram.hd.wallpapers.presentation.subscribe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class GradientTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs), MyView {

    val colors = intArrayOf(
        Color.parseColor("#FFED4A"),
        Color.parseColor("#FFA800"),
        Color.parseColor("#FFD600")
    )

    override fun show(text: CharSequence) {
        setTextColor(Color.parseColor("#FFCC00"))
//        val gradientText = SpannableString(text)
//        gradientText.setSpan(
//            LinearGradientForegroundSpan(
//                Color.parseColor("#FFED4A"),
//                Color.parseColor("#FFD600"),
//                paint.measureText(gradientText.toString()).toInt()
//            ),
//            0, gradientText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        val sb = SpannableStringBuilder()
//        sb.append(gradientText)
        setText(text)
    }

}