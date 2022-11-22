package wallgram.hd.wallpapers.data.settings

import android.content.Context
import com.bumptech.glide.Glide
import java.io.File
import java.lang.Math.log10
import java.text.DecimalFormat
import kotlin.math.pow

interface FileCacheSource {

    suspend fun fileSize(size: Long): String
    suspend fun folderSize(): Long
    suspend fun clear()

    class Base(private var context: Context) : FileCacheSource {

        override suspend fun fileSize(size: Long): String {
            if (size <= 0) return "0 KB"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups =
                (kotlin.math.log10(size.toDouble()) / kotlin.math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
        }

        override suspend fun folderSize() = getFolderSize(context.cacheDir)

        override suspend fun clear() {
            Glide.get(context).clearDiskCache()
        }

        private fun getFolderSize(dir: File): Long {
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

}

