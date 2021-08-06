package wallgram.hd.wallpapers.util.download.validator

import okhttp3.ResponseBody
import retrofit2.Response
import wallgram.hd.wallpapers.util.download.utils.contentLength
import java.io.File

object SimpleValidator : Validator {
    override fun validate(file: File, response: Response<ResponseBody>): Boolean {
        return file.length() == response.contentLength()
    }
}