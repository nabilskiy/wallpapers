package wallgram.hd.wallpapers.util

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.internal.ViewUtils.requestApplyInsetsWhenAttached
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.views.loadingbutton.animatedDrawables.ProgressType
import wallgram.hd.wallpapers.views.loadingbutton.customViews.ProgressButton

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

private fun defaultColor(context: Context) = ContextCompat.getColor(context, android.R.color.black)

private fun defaultDoneImage(resources: Resources) =
    BitmapFactory.decodeResource(resources, R.drawable.ic_pregnant_woman_white_48dp)

fun RadioGroup.setCustomChecked(id: Int, listener: RadioGroup.OnCheckedChangeListener){
    setOnCheckedChangeListener(null)
    check(id)
    setOnCheckedChangeListener(listener)
}

fun ProgressButton.morphDoneAndRevert(
    context: Context,
    fillColor: Int = defaultColor(context),
    bitmap: Bitmap = defaultDoneImage(context.resources),
    doneTime: Long = 3000,
    revertTime: Long = 4000
) {
    progressType = ProgressType.INDETERMINATE
    startAnimation()
    Handler().run {
        postDelayed({ doneLoadingAnimation(fillColor, bitmap) }, doneTime)
        postDelayed(::revertAnimation, revertTime)
    }
}

fun ProgressButton.morphAndRevert(revertTime: Long = 3000, startAnimationCallback: () -> Unit = {}) {
    startAnimation(startAnimationCallback)
    Handler().postDelayed(::revertAnimation, revertTime)
}

fun ProgressButton.morphStopRevert(stopTime: Long = 1000, revertTime: Long = 2000) {
    startAnimation()
    Handler().postDelayed(::stopAnimation, stopTime)
    Handler().postDelayed(::revertAnimation, revertTime)
}

fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
    duration = 1500
    startDelay = 500
    addUpdateListener { animation ->
        progressButton.setProgress(animation.animatedValue as Float)
    }
}

@ExperimentalCoroutinesApi
fun EditText.afterTextChangedFlow(): Flow<Editable?> = callbackFlow {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            offer(s)
        }

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int, count: Int, after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int, before: Int, count: Int
        ) {
        }
    }
    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) }
}

 fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop*3)
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

fun TextView.maxLength(max: Int){
    this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max))
}

fun AppCompatEditText.setTextFutureExt(text: String) =
    setText(
        PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
    )

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
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

fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) {

    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }

    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)