package wallgram.hd.wallpapers.util.download.downloader

import okhttp3.ResponseBody
import retrofit2.Response
import wallgram.hd.wallpapers.util.download.utils.isSupportRange

object DefaultDispatcher : Dispatcher {

    override fun dispatch(response: Response<ResponseBody>): Downloader {
        return if (response.isSupportRange()) {
            RangeDownloader()
        } else {
            NormalDownloader()
        }
    }
}