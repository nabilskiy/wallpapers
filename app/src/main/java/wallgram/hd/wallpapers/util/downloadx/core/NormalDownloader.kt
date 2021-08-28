package wallgram.hd.wallpapers.util.downloadx.core

import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import retrofit2.Response
import wallgram.hd.wallpapers.App.Companion.context
import wallgram.hd.wallpapers.util.downloadx.utils.*
import java.io.File

@OptIn(ObsoleteCoroutinesApi::class)
class NormalDownloader(coroutineScope: CoroutineScope) : BaseDownloader(coroutineScope) {
    companion object {
        private const val BUFFER_SIZE = 8192L
    }

    private var alreadyDownloaded = false

    private lateinit var file: File
    private lateinit var shadowFile: File

    override suspend fun download(
        downloadParam: DownloadParam,
        downloadConfig: DownloadConfig,
        response: Response<ResponseBody>
    ) {
        try {
            file = downloadParam.file()
            shadowFile = file.shadow()

            val contentLength = response.contentLength()
            val isChunked = response.isChunked()

            downloadPrepare(downloadParam, contentLength)

            if (alreadyDownloaded) {
                this.downloadSize = contentLength
                this.totalSize = contentLength
                this.isChunked = isChunked
            } else {
                this.totalSize = contentLength
                this.downloadSize = 0
                this.isChunked = isChunked
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
                    startDownload(response.body()!!)
                else startDownloadQ(response.body()!!, downloadParam)
            }
        } finally {
            response.closeQuietly()
        }
    }

    private fun downloadPrepare(downloadParam: DownloadParam, contentLength: Long) {
        //make sure dir is exists
        val fileDir = downloadParam.dir()
        if (!fileDir.exists() || !fileDir.isDirectory) {
            fileDir.mkdirs()
        }

        if (file.exists()) {
            if (file.length() == contentLength) {
                alreadyDownloaded = true
            } else {
                file.delete()
                shadowFile.recreate()
            }
        } else {
            shadowFile.recreate()
        }
    }

    private suspend fun startDownload(body: ResponseBody) = coroutineScope {
        val deferred = async(Dispatchers.IO) {
            val source = body.source()
            val sink = shadowFile.sink().buffer()
            val buffer = sink.buffer

            var readLen = source.read(buffer, BUFFER_SIZE)
            while (isActive && readLen != -1L) {
                downloadSize += readLen
                readLen = source.read(buffer, BUFFER_SIZE)
            }
        }
        deferred.await()

        if (isActive) {
            shadowFile.renameTo(file)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun startDownloadQ(body: ResponseBody, downloadParam: DownloadParam) = coroutineScope {
        val deferred = async(Dispatchers.IO) {
            val source = body.source()

            val values = ContentValues().apply{
                put(MediaStore.MediaColumns.DISPLAY_NAME, downloadParam.saveName)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "Akspic")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri =
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

            uri?.let{
                resolver.openOutputStream(uri)?.use { outputStream ->
                    val sink = outputStream.sink().buffer()
                    val buffer = sink.buffer

                    var readLen = source.read(buffer, BUFFER_SIZE)
                    while (isActive && readLen != -1L) {
                        downloadSize += readLen
                        readLen = source.read(buffer, BUFFER_SIZE)
                    }
                    sink.close()
                }
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            }


        }
        deferred.await()

        if (isActive) {
            shadowFile.renameTo(file)
        }
    }
}