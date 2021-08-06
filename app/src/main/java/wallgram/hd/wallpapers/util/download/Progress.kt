package wallgram.hd.wallpapers.util.download

import wallgram.hd.wallpapers.util.download.utils.formatSize
import wallgram.hd.wallpapers.util.download.utils.ratio


class Progress(
        var downloadSize: Long = 0,
        var totalSize: Long = 0,
        var isChunked: Boolean = false
) {
    fun totalSizeStr(): String {
        return totalSize.formatSize()
    }

    fun downloadSizeStr(): String {
        return downloadSize.formatSize()
    }

    fun percent(): Double {
        check(!isChunked) { "Chunked can not get percent!" }

        return downloadSize ratio totalSize
    }

    fun percentStr(): String {
        return "${percent()}%"
    }
}