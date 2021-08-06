package wallgram.hd.wallpapers.util.download.task

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Observable
import wallgram.hd.wallpapers.util.download.downloader.Dispatcher
import wallgram.hd.wallpapers.util.download.DEFAULT_SAVE_PATH
import wallgram.hd.wallpapers.util.download.Progress
import wallgram.hd.wallpapers.util.download.request.Request
import wallgram.hd.wallpapers.util.download.storage.Storage
import wallgram.hd.wallpapers.util.download.utils.fileName
import wallgram.hd.wallpapers.util.download.validator.Validator
import wallgram.hd.wallpapers.util.download.watcher.Watcher

class TaskInfo(
        val task: Task,
        val header: Map<String, String>,
        val maxConCurrency: Int,
        val rangeSize: Long,
        val dispatcher: Dispatcher,
        val validator: Validator,
        val storage: Storage,
        val request: Request,
        val watcher: Watcher
) {
    fun start(): Flowable<Progress> {
        //Before start download, we should load task first.
        storage.load(task)

        //Identify if the task is being watched.
        var watchFlag = false

        return request.get(task.url, header)
                .doOnNext {
                    check(it.isSuccessful) { "Request failed!" }

                    if (task.saveName.isEmpty()) {
                        task.saveName = it.fileName()
                    }
                    if (task.savePath.isEmpty()) {
                        task.savePath = DEFAULT_SAVE_PATH
                    }

                    try {
                        //Watch task, should be done when the task
                        //has save path and save name.
                        watcher.watch(task)
                        watchFlag = true
                    } catch (e: Throwable) {
                        throw e
                    }

                    //save task info
                    storage.save(task)
                }
                .flatMap {
                    dispatcher.dispatch(it).download(this, it)
                }
                .doFinally {
                    //unwatch task
                    if (watchFlag) {
                        watcher.unwatch(task)
                    }
                }
    }
}