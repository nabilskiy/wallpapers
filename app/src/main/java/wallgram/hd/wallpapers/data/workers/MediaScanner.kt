package wallgram.hd.wallpapers.data.workers

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import wallgram.hd.wallpapers.util.getMimeType
import wallgram.hd.wallpapers.util.getUri
import java.io.File

internal object MediaScanner {
    private fun broadcastMediaMounted(context: Context?, uri: Uri?) {
        try {
            context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, uri))
        } catch (e: Exception) {
        }
    }

    @Suppress("DEPRECATION")
    private fun broadcastScanFile(context: Context?, uri: Uri?) {
        try {
            context?.sendBroadcast(
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply { data = uri })
        } catch (e: Exception) {
        }
    }

    private fun sendBroadcasts(context: Context?, uri: Uri?) {
        broadcastMediaMounted(context, uri)
        broadcastScanFile(context, uri)
    }

    fun scan(context: Context?, path: String) {
        try {
            val file = File(path)
            val fileUri = file.getUri(context) ?: Uri.fromFile(file)
            try {
                MediaScannerConnection.scanFile(
                    context, arrayOf(path), arrayOf(file.getMimeType("image/*"))
                ) { _, uri -> sendBroadcasts(context, uri) }
            } catch (e: Exception) {
                sendBroadcasts(context, fileUri)
            }
        } catch (e: Exception) {
        }
    }
}
