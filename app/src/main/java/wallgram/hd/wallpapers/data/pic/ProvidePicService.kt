package wallgram.hd.wallpapers.data.pic

import wallgram.hd.wallpapers.core.data.MakeService
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder

interface ProvidePicService {

    fun picService(): PicService

    class Base(
        retrofitBuilder: ProvideRetrofitBuilder,
    ) : MakeService.Abstract(
        retrofitBuilder
    ), ProvidePicService {
        override fun picService(): PicService = service(PicService::class.java)
    }

}