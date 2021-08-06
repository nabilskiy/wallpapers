package wallgram.hd.wallpapers.util.download.request

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response

interface Request {
    fun get(url: String, headers: Map<String, String>): Flowable<Response<ResponseBody>>
}