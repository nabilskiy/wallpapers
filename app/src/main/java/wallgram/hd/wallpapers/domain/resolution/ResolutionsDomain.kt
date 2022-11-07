package wallgram.hd.wallpapers.domain.resolution

import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
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
            private val changeResolution: ChangeResolution,
            private val resourceProvider: ResourceProvider
        ) : Mapper<ResolutionsUi> {
            override fun map(
                list: List<String>
            ): ResolutionsUi {
                val finalList = mutableListOf<ItemUi>()

                finalList.addAll(list.mapIndexed { index, s ->

                    val title = if (index == 0)
                        resourceProvider.string(R.string.my_screen, s)
                    else s

                    ResolutionUi(
                        title, s,
                        cacheDataSource.isSelected(s), changeResolution
                    )

                })
                return ResolutionsUi.Base(finalList)
            }
        }
    }
}