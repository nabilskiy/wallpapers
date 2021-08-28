package wallgram.hd.wallpapers.util

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

@ExperimentalCoroutinesApi
fun EditText.afterTextChangedFlow(): Flow<Editable?>
        = callbackFlow {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            offer(s)
        }
        override fun beforeTextChanged(s: CharSequence?,
                                       start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?,
                                   start: Int, before: Int, count: Int) {}
    }
    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun AppCompatTextView.setTextFutureExt(text: String) =
        setTextFuture(
                PrecomputedTextCompat.getTextFuture(
                        text,
                        TextViewCompat.getTextMetricsParams(this),
                        null
                )
        )

fun AppCompatEditText.setTextFutureExt(text: String) =
        setText(
                PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
        )

fun View.slide(direction: Int){
    val animate = TranslateAnimation(0F, if(direction == 0) -width.toFloat() else width.toFloat(),0F,0F)
            .apply {
                duration = 500
                fillAfter = true
            }
    this.startAnimation(animate)
    isVisible = direction != 0

}

fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
    return fragmentManager.findFragmentByTag("f$currentItem")
}

fun ViewPager2.findFragmentAtPosition(
    fragmentManager: FragmentManager,
    position: Int
): Fragment? {
    return fragmentManager.findFragmentByTag("f$position")
}