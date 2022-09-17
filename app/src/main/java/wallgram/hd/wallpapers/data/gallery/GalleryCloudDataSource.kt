package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.data.CloudDataSource
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.model.ServerResponse

interface GalleryCloudDataSource {

    suspend fun gallery(wallpaperRequest: WallpaperRequest, page: Int): ServerResponse<GalleryCloud.Base>
    suspend fun search(query: String, page: Int): ServerResponse<GalleryCloud.Base>

    class Base(
        private val service: GalleryService,
        handleError: HandleError
    ) : CloudDataSource.Abstract(handleError), GalleryCloudDataSource {

        override suspend fun gallery(wallpaperRequest: WallpaperRequest, page: Int) = handle {
            wallpaperRequest.getRequest(service, page)
        }

        override suspend fun search(query: String, page: Int) = handle {
            service.search(query, page)
        }


    }

}