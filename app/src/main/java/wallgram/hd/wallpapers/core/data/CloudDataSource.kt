package wallgram.hd.wallpapers.core.data

import wallgram.hd.wallpapers.core.HandleError

interface CloudDataSource {

    suspend fun <T> handle(block: suspend () -> T): T

    abstract class Abstract(
        private val handleError: HandleError
    ) : CloudDataSource {

        override suspend fun <T> handle(block: suspend () -> T): T =
            try {
                block.invoke()
            } catch (error: Exception) {
                throw handleError.handle(error)
            }
    }
}