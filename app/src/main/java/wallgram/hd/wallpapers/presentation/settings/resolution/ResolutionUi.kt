package wallgram.hd.wallpapers.presentation.settings.resolution

import wallgram.hd.wallpapers.data.resolution.ChangeResolution
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class ResolutionUi(
    private val text: String,
    private val value: String,
    private val isSelected: Boolean,
    private val changeResolution: ChangeResolution
) : ItemUi {

    override fun type(): Int = 16

    override fun show(vararg views: MyView) {
        views[0].show(text)
        views[0].check(isSelected)
        views[0].handleClick {
            changeResolution.changeResolution(value)
        }
    }

    override fun id(): String = text

    override fun content(): String = "" + isSelected
}