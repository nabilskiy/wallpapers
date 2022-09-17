package wallgram.hd.wallpapers.core.data

import wallgram.hd.wallpapers.core.HandleError

interface CacheDataSource {

    suspend fun <T> handle(block: suspend () -> T): T

    abstract class Abstract(
        private val handleError: HandleError
    ) : CacheDataSource {

        override suspend fun <T> handle(block: suspend () -> T): T =
            try {
                block.invoke()
            } catch (error: Exception) {
                throw handleError.handle(error)
            }
    }
}