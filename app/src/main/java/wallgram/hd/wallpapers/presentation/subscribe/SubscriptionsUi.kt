package wallgram.hd.wallpapers.presentation.subscribe

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import java.lang.NumberFormatException

interface SubscriptionsUi {

    fun <T> map(mapper: Mapper<T>): T
    fun map(mapper: wallgram.hd.wallpapers.core.Mapper.Unit<List<ItemUi>>)

    class Base(private val list: List<ItemUi>) : SubscriptionsUi {
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

        class Id(private val id: Int) : Mapper<Int> {
            override fun map(data: List<ItemUi>): Int =
                data.indexOfFirst { id == it.id().toInt() }
        }

    }
}