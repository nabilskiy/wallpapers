package wallgram.hd.wallpapers.util

import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class CacheUtils {

    fun getFileSize(size: Long): String {
        if (size <= 0) return "0 KB"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun getFolderSize(dir: File): Long {
        var size: Long = 0
        dir.listFiles()?.let {
            for (file in it) {
                size += if (file.isFile) file.length()
                else getFolderSize(file)
            }
            return size
        }
        return size
    }

}