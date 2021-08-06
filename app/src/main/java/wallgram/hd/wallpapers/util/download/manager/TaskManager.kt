package wallgram.hd.wallpapers.util.download.manager

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.rxkotlin.subscribeBy
import wallgram.hd.wallpapers.util.download.Progress
import wallgram.hd.wallpapers.util.download.delete
import wallgram.hd.wallpapers.util.download.file
import wallgram.hd.wallpapers.util.download.storage.Storage
import wallgram.hd.wallpapers.util.download.task.Task
import wallgram.hd.wallpapers.util.download.utils.safeDispose
import java.util.concurrent.TimeUnit

import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

class TaskManager(
        private val task: Task,
        private val storage: Storage,
        private val connectFlowable: ConnectableFlowable<Progress>,
        private val notificationCreator: NotificationCreator,
        taskRecorder: TaskRecorder,
        val taskLimitation: TaskLimitation
) {

    init {
        notificationCreator.init(task)
    }

    //For download use
    private val downloadHandler by lazy { StatusHandler(task) }

    //For record use
    private val recordHandler by lazy { StatusHandler(task, taskRecorder) }

    //For notification use
    private val notificationHandler by lazy {
        StatusHandler(task, logTag = "Notification") {
            val notification = notificationCreator.create(task, it)
            showNotification(task, notification)
        }
    }

    //Download disposable
    private var disposable: Disposable? = null
    private var downloadDisposable: Disposable? = null
    private var recordDisposable: Disposable? = null
    private var notificationDisposable: Disposable? = null

    /**
     * @param tag As the unique identifier for this subscription
     * @param receiveLastStatus If true, the last status will be received after subscribing
     */
    internal fun addCallback(tag: Any, receiveLastStatus: Boolean, callback: (Status) -> Unit) {
        downloadHandler.addCallback(tag, receiveLastStatus, callback)
    }

    internal fun removeCallback(tag: Any) {
        downloadHandler.removeCallback(tag)
    }

    internal fun currentStatus() = downloadHandler.currentStatus

    internal fun getFile() = task.file(storage)

    internal fun innerStart() {
        if (isStarted()) {
            return
        }

        subscribeNotification()
        subscribeRecord()
        subscribeDownload()

        disposable = connectFlowable.connect()
    }

    private fun subscribeDownload() {
        downloadDisposable = connectFlowable
                .doOnSubscribe { downloadHandler.onStarted() }
                .subscribeOn(mainThread())
                .observeOn(mainThread())
                .doOnNext { downloadHandler.onDownloading(it) }
                .doOnComplete { downloadHandler.onCompleted() }
                .doOnError { downloadHandler.onFailed(it) }
                .doOnCancel { downloadHandler.onPaused() }
                .subscribeBy()
    }

    private fun subscribeRecord() {
        recordDisposable = connectFlowable.sample(10, SECONDS)
                .doOnSubscribe { recordHandler.onStarted() }
                .doOnNext { recordHandler.onDownloading(it) }
                .doOnComplete { recordHandler.onCompleted() }
                .doOnError { recordHandler.onFailed(it) }
                .doOnCancel { recordHandler.onPaused() }
                .subscribeBy()
    }


    private fun subscribeNotification() {
        notificationDisposable = connectFlowable.sample(500, TimeUnit.MILLISECONDS)
                .doOnSubscribe { notificationHandler.onStarted() }
                .doOnNext { notificationHandler.onDownloading(it) }
                .doOnComplete { notificationHandler.onCompleted() }
                .doOnError { notificationHandler.onFailed(it) }
                .doOnCancel { notificationHandler.onPaused() }
                .subscribeBy()
    }

    internal fun innerStop() {
        //send pause status
        notificationHandler.onPaused()
        downloadHandler.onPaused()
        recordHandler.onPaused()

        notificationDisposable.safeDispose()
        recordDisposable.safeDispose()
        downloadDisposable.safeDispose()
        disposable.safeDispose()
    }

    internal fun innerDelete() {
        innerStop()

        task.delete(storage)

        //send delete status
        downloadHandler.onDeleted()
        notificationHandler.onDeleted()
        recordHandler.onDeleted()

        cancelNotification(task)
    }

    /**
     * Send Pending status
     */
    internal fun innerPending() {
        downloadHandler.onPending()
        recordHandler.onPending()
        notificationHandler.onPending()
    }

    private fun isStarted(): Boolean {
        return disposable != null && !disposable!!.isDisposed
    }
}