package wallgram.hd.wallpapers.util.downloadx

import wallgram.hd.wallpapers.util.downloadx.utils.formatSize
import wallgram.hd.wallpapers.util.downloadx.utils.ratio


class Progress(
    var downloadSize: Long = 0,
    var totalSize: Long = 0,

    var isChunked: Boolean = false
) {

    fun isComplete(): Boolean {
        return totalSize > 0 && totalSize == downloadSize
    }

    /**
     * Return total size str. eg: 10M
     */
    fun totalSizeStr(): String {
        return totalSize.formatSize()
    }

    /**
     * Return download size str. eg: 3M
     */
    fun downloadSizeStr(): String {
        return downloadSize.formatSize()
    }

    /**
     * Return percent number.
     */
    fun percent(): Double {
        if (isChunked) return 0.0
        return downloadSize ratio totalSize
    }

    /**
     * Return percent string.
     */
    fun percentStr(): String {
        return "${percent()}%"
    }
}