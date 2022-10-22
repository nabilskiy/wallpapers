package wallgram.hd.wallpapers.domain.resolution

import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.resolution.ChangeResolution
import wallgram.hd.wallpapers.data.resolution.ResolutionCacheDataSource
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionUi
import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionsUi

interface ResolutionsDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val list: List<String>
    ) : ResolutionsDomain {

        override fun <T> map(mapper: Mapper<T>) = mapper.map(list)
    }

    interface Mapper<T> {
        fun map(list: List<String>): T

        class Base(
            private val cacheDataSource: ResolutionCacheDataSource,
            private val changeResolution: ChangeResolution
        ) : Mapper<ResolutionsUi> {
            override fun map(
                list: List<String>
            ): ResolutionsUi {
                val finalList = mutableListOf<ItemUi>()

                finalList.addAll(list.map {
                   ResolutionUi(
                        it,
                        cacheDataSource.isSelected(it), changeResolution
                    )

                })
                return ResolutionsUi.Base(finalList)
            }
        }
    }
}