package wallgram.hd.wallpapers.domain.filters

import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.carousel.CarouselUi
import wallgram.hd.wallpapers.presentation.categories.HeaderUi
import wallgram.hd.wallpapers.presentation.categories.HeaderViewType


interface CategoriesDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val categories: List<CategoryDomain>,
        private val colors: List<ItemUi> = listOf()
    ) : CategoriesDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(categories, colors)
    }

    interface Mapper<T> {
        fun map(categories: List<CategoryDomain>, colors: List<ItemUi>): T

        class Base(
            private val categoryMapper: CategoryDomain.Mapper<FilterUi.Base>,
            private val resourceProvider: ResourceProvider
        ) : Mapper<FiltersUi> {

            override fun map(categories: List<CategoryDomain>, colors: List<ItemUi>): FiltersUi {
                val finalList =
                    mutableListOf<ItemUi>(HeaderUi(resourceProvider.string(R.string.discover)))
                finalList.add(CarouselUi(colors))
                finalList.add(
                    HeaderUi(
                        resourceProvider.string(R.string.category),
                        HeaderViewType.Small()
                    )
                )
                finalList.addAll(categories.map {
                    it.map(categoryMapper)
                })
                return FiltersUi.Base(finalList)
            }
        }

    }
}