package wallgram.hd.wallpapers.util.download.manager

import wallgram.hd.wallpapers.util.assertMainThreadWithResult
import wallgram.hd.wallpapers.util.download.DEFAULT_MAX_CONCURRENCY
import wallgram.hd.wallpapers.util.download.DEFAULT_RANGE_SIZE
import wallgram.hd.wallpapers.util.download.RANGE_CHECK_HEADER
import wallgram.hd.wallpapers.util.download.downloader.DefaultDispatcher
import wallgram.hd.wallpapers.util.download.downloader.Dispatcher
import wallgram.hd.wallpapers.util.download.request.Request
import wallgram.hd.wallpapers.util.download.request.RequestImpl
import wallgram.hd.wallpapers.util.download.storage.SimpleStorage
import wallgram.hd.wallpapers.util.download.storage.Storage
import wallgram.hd.wallpapers.util.download.task.Task
import wallgram.hd.wallpapers.util.download.validator.SimpleValidator
import wallgram.hd.wallpapers.util.download.validator.Validator
import wallgram.hd.wallpapers.util.download.watcher.Watcher
import wallgram.hd.wallpapers.util.download.watcher.WatcherImpl
import wallgram.hd.wallpapers.util.ensureMainThread
import java.io.File

@JvmOverloads
fun String.manager(
        header: Map<String, String> = RANGE_CHECK_HEADER,
        maxConCurrency: Int = DEFAULT_MAX_CONCURRENCY,
        rangeSize: Long = DEFAULT_RANGE_SIZE,
        dispatcher: Dispatcher = DefaultDispatcher,
        validator: Validator = SimpleValidator,
        storage: Storage = SimpleStorage,
        request: Request = RequestImpl,
        watcher: Watcher = WatcherImpl,
        notificationCreator: NotificationCreator = EmptyNotification,
        recorder: TaskRecorder = EmptyRecorder,
        taskLimitation: TaskLimitation = BasicTaskLimitation.of()
): TaskManager {
    return Task(this).manager(
            header = header,
            maxConCurrency = maxConCurrency,
            rangeSize = rangeSize,
            dispatcher = dispatcher,
            validator = validator,
            storage = storage,
            request = request,
            watcher = watcher,
            notificationCreator = notificationCreator,
            recorder = recorder,
            taskLimitation = taskLimitation
    )
}

@JvmOverloads
fun Task.manager(
        header: Map<String, String> = RANGE_CHECK_HEADER,
        maxConCurrency: Int = DEFAULT_MAX_CONCURRENCY,
        rangeSize: Long = DEFAULT_RANGE_SIZE,
        dispatcher: Dispatcher = DefaultDispatcher,
        validator: Validator = SimpleValidator,
        storage: Storage = SimpleStorage,
        request: Request = RequestImpl,
        watcher: Watcher = WatcherImpl,
        notificationCreator: NotificationCreator = EmptyNotification,
        recorder: TaskRecorder = EmptyRecorder,
        taskLimitation: TaskLimitation = BasicTaskLimitation.of()
): TaskManager {
    return TaskManagerPool.obtain(
            task = this,
            header = header,
            maxConCurrency = maxConCurrency,
            rangeSize = rangeSize,
            dispatcher = dispatcher,
            validator = validator,
            storage = storage,
            request = request,
            watcher = watcher,
            notificationCreator = notificationCreator,
            recorder = recorder,
            taskLimitation = taskLimitation
    )
}

fun TaskManager.subscribe(function: (Status) -> Unit): Any {
    return assertMainThreadWithResult {
        val tag = Any()
        addCallback(tag, true, function)
        return@assertMainThreadWithResult tag
    }
}

fun TaskManager.dispose(tag: Any) {
    ensureMainThread {
        removeCallback(tag)
    }
}

fun TaskManager.currentStatus(): Status {
    return assertMainThreadWithResult {
        return@assertMainThreadWithResult currentStatus()
    }
}

fun TaskManager.start() {
    ensureMainThread {
        taskLimitation.start(this)
    }
}

fun TaskManager.stop() {
    ensureMainThread {
        taskLimitation.stop(this)
    }
}

fun TaskManager.delete() {
    ensureMainThread {
        taskLimitation.delete(this)
    }
}

fun TaskManager.file(): File {
    return getFile()
}