package wallgram.hd.wallpapers.data.colors

import wallgram.hd.wallpapers.presentation.colors.ColorsUi
import javax.inject.Inject

interface ColorsRepository {

    fun colors(): ColorsUi

    class Base @Inject constructor(
        private val colorsDataSource: ColorsDataSource,
        private val mapper: ColorMapper
    ): ColorsRepository {
        override fun colors(): ColorsUi{
            val list = colorsDataSource.colors().map {
               it.map(mapper)
            }
            return ColorsUi.Base(list)
        }
    }

}