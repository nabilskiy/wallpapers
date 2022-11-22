package wallgram.hd.wallpapers.data.workers

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DownloadProgressLiveData(private val context: Context, private val requestId: Long) :
    LiveData<DownloadItem>(), CoroutineScope {

    private val downloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onActive() {
        super.onActive()
        launch {
            while (isActive) {
                val query = DownloadManager.Query().setFilterById(requestId)
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    Log.d("Status", "$status")
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL,
                        DownloadManager.STATUS_PENDING,
                        DownloadManager.STATUS_FAILED,
                        DownloadManager.STATUS_PAUSED -> postValue(DownloadItem(status = status))
                        else -> {
                            val bytesDownloadedSoFar =
                                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            val totalSizeBytes =
                                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            postValue(
                                DownloadItem(
                                    bytesDownloadedSoFar.toLong(),
                                    totalSizeBytes.toLong(),
                                    status
                                )
                            )
                        }
                    }
                    if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED)
                        cancel()
                } else {
                    postValue(DownloadItem(status = 32))
                    cancel()
                }
                cursor.close()
                delay(300)
            }
        }
    }


    override fun onInactive() {
        super.onInactive()
        job.cancel()

    }

}

