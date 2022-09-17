package wallgram.hd.wallpapers.presentation.wallpaper.info

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

interface InfoListUi : Mapper.Unit<Mapper.Unit<List<ItemUi>>> {

    class Base(private val list: List<ItemUi>) : InfoListUi {
        override fun map(data: Mapper.Unit<List<ItemUi>>) = data.map(list)
    }
}