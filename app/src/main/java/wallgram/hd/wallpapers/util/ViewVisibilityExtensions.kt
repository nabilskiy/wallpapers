package wallgram.hd.wallpapers.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.visible() {

    animate().alpha(1f).setDuration(0).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            visibility = View.VISIBLE
        }
    })

}

fun View.hide() {

        animate().alpha(0f).setDuration(0).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
}