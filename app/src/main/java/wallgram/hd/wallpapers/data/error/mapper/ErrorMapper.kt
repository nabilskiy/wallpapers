package wallgram.hd.wallpapers.data.error.mapper

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.error.*
import javax.inject.Inject

class ErrorMapper @Inject constructor() : ErrorMapperInterface {

    override fun getErrorString(errorId: Int): String {
        return wallgram.hd.wallpapers.App.context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(

        )
}
