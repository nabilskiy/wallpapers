package wallgram.hd.wallpapers.util.download.downloader

import okhttp3.ResponseBody
import retrofit2.Response

interface Dispatcher {
    fun dispatch(response: Response<ResponseBody>): Downloader
}