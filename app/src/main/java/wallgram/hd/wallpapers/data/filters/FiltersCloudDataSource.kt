package wallgram.hd.wallpapers.data.filters

import wallgram.hd.wallpapers.core.data.CloudDataSource
import wallgram.hd.wallpapers.core.HandleError

interface FiltersCloudDataSource {

    suspend fun filters(): List<FiltersCloud>

    class Base(
        private val filtersService: FiltersService,
        handleError: HandleError
    ) : CloudDataSource.Abstract(handleError), FiltersCloudDataSource {

        override suspend fun filters() = handle {
            filtersService.filters()
        }
    }

}