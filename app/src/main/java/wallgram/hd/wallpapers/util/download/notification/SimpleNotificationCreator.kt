package wallgram.hd.wallpapers.util.download.notification

import android.app.Notification
import androidx.core.app.NotificationCompat
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.util.download.claritypotion.ClarityPotion.Companion.clarityPotion
import wallgram.hd.wallpapers.util.download.manager.*
import wallgram.hd.wallpapers.util.download.notification.NotificationActionService.Companion.cancelAction
import wallgram.hd.wallpapers.util.download.notification.NotificationActionService.Companion.startAction
import wallgram.hd.wallpapers.util.download.notification.NotificationActionService.Companion.stopAction
import wallgram.hd.wallpapers.util.download.task.Task
import wallgram.hd.wallpapers.util.download.utils.log

class SimpleNotificationCreator : NotificationCreator {
    private val channelId = "RxDownload"
    private val channelName = "RxDownload"
    private val channelDesc = "RxDownload"

    private lateinit var task: Task
    private val builderHelper by lazy { BuilderHelper(channelId, task) }

    override fun init(task: Task) {
        this.task = task

        if (!isEnableNotification()) {
            "Notification not enable".log()
        }

        createNotificationChannel(channelId, channelName, channelDesc)
    }

    override fun create(task: Task, status: Status): Notification? {
        return builderHelper.get(status)?.build()
    }

    class BuilderHelper(private val channelId: String, private val task: Task) {
        private val builderMap = mutableMapOf<Status, NotificationCompat.Builder>()

        private val pendingContent by lazy { clarityPotion.getString(R.string.notification_pending_text) }
        private val startedContent by lazy { clarityPotion.getString(R.string.notification_started_text) }
        private val pausedContent by lazy { clarityPotion.getString(R.string.notification_paused_text) }
        private val failedContent by lazy { clarityPotion.getString(R.string.notification_failed_text) }
        private val completedContent by lazy { clarityPotion.getString(R.string.notification_completed_text) }

        private val pendingActions by lazy { listOf(stopAction(task), cancelAction(task)) }
        private val startedActions by lazy { listOf(stopAction(task), cancelAction(task)) }
        private val downloadingActions by lazy { listOf(stopAction(task), cancelAction(task)) }
        private val pausedActions by lazy { listOf(startAction(task), cancelAction(task)) }
        private val failedActions by lazy { listOf(startAction(task), cancelAction(task)) }

        fun get(status: Status): NotificationCompat.Builder? {
            val builder = getBuilder(status)

            when (status) {
                is Downloading -> builder.setProgress(
                        status.progress.totalSize.toInt(),
                        status.progress.downloadSize.toInt(),
                        status.progress.isChunked
                )
                //Do not need notification
                is Normal -> return null
                is Deleted -> return null
            }
            return builder
        }

        private fun getBuilder(status: Status): NotificationCompat.Builder {
            val builder = builderMap[status]

            if (builder == null) {
                val (content, actions, icon) = when (status) {
                    is Normal -> Triple("", emptyList(), 0)
                    is Pending -> Triple(pendingContent, pendingActions, R.drawable.ic_download_file)
                    is Started -> Triple(startedContent, startedActions, R.drawable.ic_download_file)
                    is Downloading -> Triple("", downloadingActions, R.drawable.ic_download_file)
                    is Paused -> Triple(pausedContent, pausedActions, R.drawable.ic_pause)
                    is Failed -> Triple(failedContent, failedActions, R.drawable.ic_pause)
                    is Completed -> Triple(completedContent, emptyList(), R.drawable.ic_completed)
                    is Deleted -> Triple("", emptyList(), 0)
                }

                val newBuilder = createNotificationBuilder(
                        channelId = channelId,
                        title = task.taskName,
                        content = content,
                        icon = icon,
                        actions = actions
                )
                builderMap[status] = newBuilder

                return newBuilder
            } else {
                return builder
            }
        }
    }
}