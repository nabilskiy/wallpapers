package wallgram.hd.wallpapers.domain.home

import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.carousel.CarouselUi
import wallgram.hd.wallpapers.presentation.categories.HeaderUi
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.home.scroll.HomeCarouselUi

interface HomesDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val filters: List<HomeDomain>,
    ) : HomesDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(filters)
    }

    interface Mapper<T> {
        fun map(categories: List<HomeDomain>): T

        class Base(private val homeMapper: HomeDomain.Mapper<HomeCarouselUi.Base>) : Mapper<FiltersUi> {
            override fun map(categories: List<HomeDomain>): FiltersUi {
                return FiltersUi.Base(categories.map { it.map(homeMapper) })
            }
        }

    }
}