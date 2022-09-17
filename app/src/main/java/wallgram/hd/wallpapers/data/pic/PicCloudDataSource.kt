package wallgram.hd.wallpapers.data.pic

import wallgram.hd.wallpapers.core.data.CloudDataSource
import wallgram.hd.wallpapers.core.HandleError

interface PicCloudDataSource {

    suspend fun pic(id: Int): PicCloud

    class Base(
        private val picService: PicService,
        handleError: HandleError
    ) : CloudDataSource.Abstract(handleError), PicCloudDataSource {

        override suspend fun pic(id: Int) = handle {
            picService.pic(id)
        }
    }

}