package wallgram.hd.wallpapers.util.download.validator

import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface Validator {
    fun validate(file: File, response: Response<ResponseBody>): Boolean
}