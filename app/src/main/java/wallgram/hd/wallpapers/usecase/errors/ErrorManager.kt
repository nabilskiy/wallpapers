package wallgram.hd.wallpapers.usecase.errors

import wallgram.hd.wallpapers.data.error.mapper.ErrorMapper
import javax.inject.Inject

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorFactory {
    override fun getError(errorCode: Int): wallgram.hd.wallpapers.data.error.Error {
        return wallgram.hd.wallpapers.data.error.Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}

