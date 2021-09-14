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
import java.io.*
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener


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

        try{
            val contentLength = response.contentLength()
            val isChunked = response.isChunked()

            this.totalSize = contentLength
            this.isChunked = isChunked
            this.downloadSize = 0

            startDownload(response.body()!!, downloadParam)
        }
        finally {
            response.closeQuietly()
        }

//        try {
//            file = downloadParam.file()
//            shadowFile = file.shadow()
//
//            val contentLength = response.contentLength()
//            val isChunked = response.isChunked()
//
//          //  downloadPrepare(downloadParam, contentLength)
//
//
//            if (alreadyDownloaded) {
//                this.downloadSize = contentLength
//                this.totalSize = contentLength
//                this.isChunked = isChunked
//            } else {
//                this.totalSize = contentLength
//                this.downloadSize = 0
//                this.isChunked = isChunked
//
//            }
//        } finally {
//            response.closeQuietly()
//        }
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


    lateinit var destURL: String
    private lateinit var outputPath: String


    private suspend fun startDownload(body: ResponseBody, downloadParam: DownloadParam) = coroutineScope {
        val deferred = async(Dispatchers.IO) {

            var total: Long = 0
            var count = 0
            val buffer = ByteArray(1024)

            val inputStream = body.byteStream()
            val outputStream: OutputStream?
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                destURL = Environment.getExternalStorageDirectory().path + File.separator + Environment.DIRECTORY_PICTURES + "/Wallgram"
                var desFile = File(destURL)
                if (!desFile.exists()) {
                    desFile.mkdir()
                }

               // destURL = destURL + File.separator + Environment.DIRECTORY_DOWNLOADS
                desFile = File(destURL)
                if (!desFile.exists())
                    desFile.mkdir()

                destURL = destURL + File.separator + downloadParam.saveName

                outputPath = destURL

                outputStream = FileOutputStream(destURL)
                while (inputStream.read(buffer).also { count = it } != -1) {
                    downloadSize += count.toLong()

                    outputStream.write(buffer, 0, count)
                }

                outputStream.flush()
                outputStream.close()

                inputStream.close()
                MediaScannerConnection.scanFile(context, arrayOf(outputPath), null
                ) { _, _ ->
                }
            } else {
                val bis = BufferedInputStream(inputStream)
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, downloadParam.saveName)
                values.put(MediaStore.Downloads.IS_PENDING, 1)

                var desDirectory = Environment.DIRECTORY_DOWNLOADS
                desDirectory = desDirectory + File.separator + "Wallgram"
                val desFile = File(desDirectory)
                if (!desFile.exists()) {
                    desFile.mkdir()
                }
                // final output path
                outputPath = desDirectory + File.separator + downloadParam.saveName

                values.put(MediaStore.MediaColumns.RELATIVE_PATH, desDirectory)
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    outputStream = context.contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        val bos = BufferedOutputStream(outputStream)
                        var bytes = bis.read(buffer)
                        while (bytes.also { count = it } != -1) {
                            downloadSize += count.toLong()

                            bos.write(buffer, 0, count)
                            bos.flush()
                            bytes = bis.read(buffer)
                        }
                        bos.close()
                    }
                    values.clear()
                    values.put(MediaStore.Downloads.IS_PENDING, 0)
                    context.contentResolver.update(uri, values, null, null)
                }
                bis.close()

            }
        }
        deferred.await()

        if (isActive) {
            shadowFile.renameTo(file)
        }
    }
}