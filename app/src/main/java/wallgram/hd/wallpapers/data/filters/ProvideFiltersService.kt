package wallgram.hd.wallpapers.data.filters

import wallgram.hd.wallpapers.core.data.MakeService
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder

interface ProvideFiltersService {

    fun filtersService(): FiltersService

    class Base(
        retrofitBuilder: ProvideRetrofitBuilder,
    ) : MakeService.Abstract(
        retrofitBuilder
    ), ProvideFiltersService {
        override fun filtersService(): FiltersService = service(FiltersService::class.java)
    }

}