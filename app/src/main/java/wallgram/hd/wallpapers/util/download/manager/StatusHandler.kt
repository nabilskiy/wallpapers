package wallgram.hd.wallpapers.util.download.manager

import wallgram.hd.wallpapers.util.download.Progress
import wallgram.hd.wallpapers.util.download.task.Task
import wallgram.hd.wallpapers.util.download.utils.log

class StatusHandler(
        private val task: Task,
        private val taskRecorder: TaskRecorder? = null,
        private val logTag: String = "",
        callback: (Status) -> Unit = {}
) {
    private val normal = Normal()
    private val pending by lazy { Pending() }
    private val started by lazy { Started() }
    private val downloading by lazy { Downloading() }
    private val paused by lazy { Paused() }
    private val completed by lazy { Completed() }
    private val failed by lazy { Failed() }
    private val deleted by lazy { Deleted() }

    var currentStatus: Status = normal

    private val callbackSafeMap = SafeIterableMap<Any, (Status) -> Unit>()

    private var currentProgress: Progress = Progress()

    init {
        callbackSafeMap.putIfAbsent(Any(), callback)
    }

    fun addCallback(tag: Any, receiveLastStatus: Boolean, callback: (Status) -> Unit) {
        callbackSafeMap.putIfAbsent(tag, callback)

        //emit last status when not normal
        if (receiveLastStatus && currentStatus != normal) {
            callback(currentStatus)
        }
    }

    fun removeCallback(tag: Any) {
        callbackSafeMap.remove(tag)
    }

    fun onPending() {
        currentStatus = pending.updateProgress()
        dispatchCallback()

        //try to insert
        taskRecorder?.insert(task)
    }

    fun onStarted() {
        currentStatus = started.updateProgress()
        dispatchCallback()

        //try to insert
        taskRecorder?.insert(task)
        taskRecorder?.update(task, currentStatus)

        "$logTag [${task.taskName}] started".log()
    }

    fun onDownloading(next: Progress) {
        //set current progress
        currentProgress = next
        currentStatus = downloading.updateProgress()
        dispatchCallback()

        taskRecorder?.update(task, currentStatus)

        "$logTag [${task.taskName}] downloading".log()
    }

    fun onCompleted() {
        currentStatus = completed.updateProgress()
        dispatchCallback()

        taskRecorder?.update(task, currentStatus)

        "$logTag [${task.taskName}] completed".log()
    }

    fun onFailed(t: Throwable) {
        currentStatus = failed.apply {
            progress = currentProgress
            throwable = t
        }
        dispatchCallback()

        taskRecorder?.update(task, currentStatus)

        "$logTag [${task.taskName}] failed".log()
    }

    fun onPaused() {
        currentStatus = paused.updateProgress()
        dispatchCallback()

        taskRecorder?.update(task, currentStatus)

        "$logTag [${task.taskName}] paused".log()
    }

    fun onDeleted() {
        //reset current progress
        currentProgress = Progress()
        currentStatus = deleted.updateProgress()
        dispatchCallback()

        //delete
        taskRecorder?.delete(task)

        "$logTag [${task.taskName}] deleted".log()
    }

    private fun dispatchCallback() {
        callbackSafeMap.forEach {
            it.value(currentStatus)
        }
    }

    private fun Status.updateProgress(): Status {
        progress = currentProgress
        return this
    }
}