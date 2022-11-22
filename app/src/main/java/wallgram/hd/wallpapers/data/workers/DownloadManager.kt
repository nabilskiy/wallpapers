package wallgram.hd.wallpapers.data.workers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import wallgram.hd.wallpapers.presentation.dialogs.UpdateDownload
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import javax.inject.Inject

interface DownloadManager {

    fun download(url: String): WallpaperDownloader.Result
    fun apply(url: String, option: Int): LiveData<WorkInfo>
    fun cancelWorkManagerTasks()
    fun cancel(id: Long)

    class Base @Inject constructor(context: Context) : DownloadManager {

        private val workManager = WorkManager.getInstance(context)
        private val downloader = WallpaperDownloader(context)

        override fun download(url: String): WallpaperDownloader.Result {
            cancelWorkManagerTasks()
            val newDownloadTask =
                downloader.download(url)


//            newDownloadTask?.let { task ->
//                workManager.enqueue(task)
//                return workManager.getWorkInfoByIdLiveData(task.id)
//            }
            return newDownloadTask
        }

        override fun apply(url: String, option: Int): LiveData<WorkInfo> {
            val newApplyTask = WallpaperApplier.buildRequest(url, option)
            newApplyTask?.let { task ->
                workManager.enqueue(task)
                return workManager.getWorkInfoByIdLiveData(task.id)
            }
            return MutableLiveData()
        }

        override fun cancelWorkManagerTasks() {
            try {
                workManager.cancelAllWork()
                workManager.pruneWork()
            } catch (e: Exception) {
            }
        }

        override fun cancel(id: Long) {
            try {
                downloader.cancel(id)
            } catch (e: Exception) {
            }
        }

    }

}