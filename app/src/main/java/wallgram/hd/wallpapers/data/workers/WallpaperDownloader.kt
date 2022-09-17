package wallgram.hd.wallpapers.data.workers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.coroutineScope
import wallgram.hd.wallpapers.presentation.wallpaper.ContextAwareWorker
import wallgram.hd.wallpapers.util.createIfDidNotExist
import wallgram.hd.wallpapers.util.filenameAndExtension
import wallgram.hd.wallpapers.util.getDefaultWallpapersDownloadFolder
import wallgram.hd.wallpapers.util.hasContent
import java.io.File

class WallpaperDownloader(context: Context, params: WorkerParameters) :
    ContextAwareWorker(context, params),
    DownloadListenerThread.DownloadListener {

    @Suppress("DEPRECATION")
    private fun downloadUsingNotificationManager(url: String, file: File): Long {
        val fileUri: Uri? = Uri.fromFile(file)
        fileUri ?: return -1L

        val downloadUsingWiFiOnly = false
        val allowedNetworkTypes =
            if (downloadUsingWiFiOnly) DownloadManager.Request.NETWORK_WIFI
            else (DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

        val request = DownloadManager.Request(Uri.parse(url))
            .apply {
                setTitle(file.name)
                setDescription("Download" + file.name)
                setDestinationUri(fileUri)
                setAllowedNetworkTypes(allowedNetworkTypes)
                setAllowedOverRoaming(!downloadUsingWiFiOnly)
                setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE
                            or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                allowScanningByMediaScanner()
            }

        val downloadManager: DownloadManager? =
            context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        downloadManager ?: return -1L

        val downloadId = try {
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
            return -1L
        }

        val thread =
            DownloadListenerThread(
                context, downloadManager, downloadId, file.absolutePath, this
            )
        thread.start()
        return downloadId
    }

    var downloadsFolder: File? = File(
        context.getDefaultWallpapersDownloadFolder().toString()
    ).apply {
        createIfDidNotExist()
    }


    override suspend fun doWork(): Result = coroutineScope {
        val url: String = inputData.getString(DOWNLOAD_URL_KEY) ?: ""
        if (!url.hasContent()) return@coroutineScope Result.failure()

        val (filename, extension) = url.filenameAndExtension
        val folder = downloadsFolder
            ?: context?.externalCacheDir ?: context?.cacheDir
        val filePath = "$folder${File.separator}$filename$extension"

        val file = File(filePath)
        if (file.exists() && file.length() > 0L) {
            onSuccess(filePath)
            val outputData = workDataOf(
                DOWNLOAD_PATH_KEY to file.absolutePath,
                DOWNLOAD_FILE_EXISTED to true
            )
            return@coroutineScope Result.success(outputData)
        }

        file.parentFile?.createIfDidNotExist()
        file.delete()

        val downloadId = downloadUsingNotificationManager(url, file)
        if (downloadId == -1L) return@coroutineScope Result.failure()

        val outputData = workDataOf(
            DOWNLOAD_PATH_KEY to filePath,
            DOWNLOAD_TASK_KEY to downloadId,
            DOWNLOAD_FILE_EXISTED to false
        )
        return@coroutineScope Result.success(outputData)
    }

    override fun onSuccess(path: String) {
        super.onSuccess(path)
        try {
            MediaScanner.scan(context, path)
        } catch (e: Exception) {
        }
    }

    override fun onFailure(exception: Exception) {
        super.onFailure(exception)
        try {
            Log.d("ERROR", exception.message ?: "ERROR")
            //context?.toast(exception.message ?: "Unexpected error!", Toast.LENGTH_LONG)
        } catch (e: Exception) {
        }
    }

    companion object {
        internal const val DOWNLOAD_PATH_KEY = "download_path"
        internal const val DOWNLOAD_URL_KEY = "download_url"
        internal const val DOWNLOAD_TASK_KEY = "download_task"
        internal const val DOWNLOAD_FILE_EXISTED = "download_file_existed"

        fun buildRequest(url: String): OneTimeWorkRequest? {
            if (!url.hasContent()) return null
            return try {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                OneTimeWorkRequest.Builder(WallpaperDownloader::class.java)
                    .setConstraints(constraints)
                    .setInputData(workDataOf(DOWNLOAD_URL_KEY to url))
                    .build()
            } catch (e: Exception) {
                null
            }
        }
    }
}
