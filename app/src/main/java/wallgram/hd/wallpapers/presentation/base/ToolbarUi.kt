package wallgram.hd.wallpapers.presentation.base

import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.util.dp

class ToolbarUi(
    private val text: String
) : ItemUi {
    override fun type() = ViewIds.TOOLBAR

    override fun show(vararg views: MyView) {
        views[0].show(text)
    }

    override fun id() = text

    override fun content() = text

}