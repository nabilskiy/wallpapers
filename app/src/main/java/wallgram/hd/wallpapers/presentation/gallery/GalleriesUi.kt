package wallgram.hd.wallpapers.presentation.gallery

import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import java.lang.NumberFormatException

interface GalleriesUi {

    fun <T> map(mapper: Mapper<T>): T
    fun map(mapper: wallgram.hd.wallpapers.core.Mapper.Unit<List<ItemUi>>)

    class Base(private val list: List<ItemUi>) : GalleriesUi {
        //  override fun map(data: Mapper.Unit<List<ItemUi>>) = data.map(list)
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(list)
        override fun map(mapper: wallgram.hd.wallpapers.core.Mapper.Unit<List<ItemUi>>) =
            mapper.map(list)
    }

    interface Mapper<T> {
        fun map(data: List<ItemUi>): T

        class Base() : Mapper<Unit>, wallgram.hd.wallpapers.core.Mapper.Unit<List<ItemUi>> {
            override fun map(data: List<ItemUi>) = Unit
        }

        class Position(private val id: Int) : Mapper<Int> {
            override fun map(data: List<ItemUi>): Int =
                try {
                    data.indexOfFirst {
                        id == it.id().toInt()
                    }
                } catch (e: NumberFormatException) {
                    0
                }
        }

    }

}