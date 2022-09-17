package wallgram.hd.wallpapers.data.workers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import wallgram.hd.wallpapers.presentation.dialogs.UpdateDownload
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import javax.inject.Inject

interface DownloadManager {

    fun download(url: String): LiveData<WorkInfo>
    fun apply(url: String, option: Int): LiveData<WorkInfo>
    fun cancelWorkManagerTasks()

    class Base @Inject constructor(context: Context) : DownloadManager {

        private val workManager = WorkManager.getInstance(context)

        override fun download(url: String): LiveData<WorkInfo> {
            val newDownloadTask =
                WallpaperDownloader.buildRequest(url)
            newDownloadTask?.let { task ->
                workManager.enqueue(task)
                return workManager.getWorkInfoByIdLiveData(task.id)
            }
            return MutableLiveData()
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

    }

}