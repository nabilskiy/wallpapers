package wallgram.hd.wallpapers.util.download.downloader

import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import wallgram.hd.wallpapers.util.download.Progress
import wallgram.hd.wallpapers.util.download.task.TaskInfo

interface Downloader {
    fun download(taskInfo: TaskInfo, response: Response<ResponseBody>): Flowable<Progress>
}