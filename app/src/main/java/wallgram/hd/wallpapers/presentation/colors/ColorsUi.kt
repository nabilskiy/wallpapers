package wallgram.hd.wallpapers.presentation.colors

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

interface ColorsUi : Mapper.Unit<Mapper.Unit<List<ItemUi>>> {

    class Base(private val list: List<ItemUi>) : ColorsUi {
        override fun map(data: Mapper.Unit<List<ItemUi>>) = data.map(list)
    }
}