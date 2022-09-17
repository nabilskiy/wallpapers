package wallgram.hd.wallpapers.presentation.base

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface Refreshing {

    fun apply(view: SwipeRefreshLayout)

    abstract class Abstract(private val refreshing: Boolean) : Refreshing {
        override fun apply(view: SwipeRefreshLayout) {
            view.isRefreshing = refreshing
        }
    }

    class Loading : Abstract(true)
    class Done : Abstract(false)
}