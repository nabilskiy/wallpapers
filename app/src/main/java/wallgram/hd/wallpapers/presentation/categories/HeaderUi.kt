package wallgram.hd.wallpapers.presentation.categories

import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.ViewIds
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.util.dp

class HeaderUi(
    private val text: String,
    private val headerViewType: HeaderViewType = HeaderViewType.Default()
) : ItemUi {
    override fun type() = ViewIds.HEADER

    override fun show(vararg views: MyView) {
        views[0].show(text)
        views[0].textSize(headerViewType.textSize())
    }

    override fun id() = text

    override fun content() = text

}