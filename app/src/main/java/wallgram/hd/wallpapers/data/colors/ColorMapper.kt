package wallgram.hd.wallpapers.data.colors

import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.colors.NavigateColor
import javax.inject.Inject

interface ColorMapper : Color.Mapper<ItemUi> {

    class Base @Inject constructor(
        private val resourceProvider: ResourceProvider,
        private val navigateColor: NavigateColor
    ) : ColorMapper {
        override fun map(
            id: Int,
            title: Int,
            color: Int,
            textColor: Int,
            startColor: Int,
            endColor: Int
        ): ItemUi {
            val name = resourceProvider.string(title)
            val value = resourceProvider.color(color)

            return ColorUi.Base(
                id, name, color, textColor, startColor, endColor, value, navigateColor
            )
        }
    }
}