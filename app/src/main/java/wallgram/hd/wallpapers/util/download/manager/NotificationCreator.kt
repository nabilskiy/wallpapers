package wallgram.hd.wallpapers.util.download.manager

import android.app.Notification
import wallgram.hd.wallpapers.util.download.task.Task

interface NotificationCreator {
    fun init(task: Task)

    fun create(task: Task, status: Status): Notification?
}