package wallgram.hd.wallpapers.presentation.settings.resolution

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

interface ResolutionsUi : Mapper.Unit<Mapper.Unit<List<ItemUi>>> {

    class Base(private val list: List<ItemUi>) : ResolutionsUi {
        override fun map(data: Mapper.Unit<List<ItemUi>>) = data.map(list)
    }
}