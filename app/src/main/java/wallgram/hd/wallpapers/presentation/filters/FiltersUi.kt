package wallgram.hd.wallpapers.presentation.filters

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

interface FiltersUi : Mapper.Unit<Mapper.Unit<List<ItemUi>>> {

    class Base(private val list: List<ItemUi>) : FiltersUi {
        override fun map(data: Mapper.Unit<List<ItemUi>>) = data.map(list)
    }
}