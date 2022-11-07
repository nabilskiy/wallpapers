package wallgram.hd.wallpapers.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.wallpaper.State
import wallgram.hd.wallpapers.util.findSuitableParent

class ChefSnackbar(
    parent: ViewGroup,
    private val content: ChefSnackbarView
) : BaseTransientBottomBar<ChefSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    fun state(state: State): ChefSnackbar = apply {
        duration = state.duration()
        content.update(state)
        show()
    }

    companion object {

        fun make(view: View): ChefSnackbar {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_snackbar_chef,
                parent,
                false
            ) as ChefSnackbarView

            return ChefSnackbar(
                parent,
                customView
            )
        }

    }

}