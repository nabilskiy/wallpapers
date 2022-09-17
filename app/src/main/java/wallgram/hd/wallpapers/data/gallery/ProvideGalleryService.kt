package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.core.data.MakeService
import wallgram.hd.wallpapers.core.data.ProvideRetrofitBuilder

interface ProvideGalleryService {

    fun galleryService(): GalleryService

    class Base(
        retrofitBuilder: ProvideRetrofitBuilder,
    ) : MakeService.Abstract(
        retrofitBuilder
    ), ProvideGalleryService {
        override fun galleryService(): GalleryService = service(GalleryService::class.java)
    }

}