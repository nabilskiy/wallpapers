package wallgram.hd.wallpapers.data.workers

data class DownloadItem(
    val bytesDownloadedSoFar: Long = -1,
    val totalSizeBytes: Long = -1,
    val status: Int
)
