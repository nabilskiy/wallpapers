package wallgram.hd.wallpapers.util.downloadx

import kotlinx.coroutines.CoroutineScope
import wallgram.hd.wallpapers.util.downloadx.helper.Default
import wallgram.hd.wallpapers.util.downloadx.core.DownloadTask
import wallgram.hd.wallpapers.util.downloadx.core.DownloadParam
import wallgram.hd.wallpapers.util.downloadx.core.DownloadConfig

fun CoroutineScope.download(
    url: String,
    saveName: String = "",
    savePath: String = Default.DEFAULT_SAVE_PATH,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    val downloadParam = DownloadParam(url, saveName, savePath)
    val task = DownloadTask(this, downloadParam, downloadConfig)
    return downloadConfig.taskManager.add(task)
}

fun CoroutineScope.download(
    downloadParam: DownloadParam,
    downloadConfig: DownloadConfig = DownloadConfig()
): DownloadTask {
    val task = DownloadTask(this, downloadParam, downloadConfig)
    return downloadConfig.taskManager.add(task)
}