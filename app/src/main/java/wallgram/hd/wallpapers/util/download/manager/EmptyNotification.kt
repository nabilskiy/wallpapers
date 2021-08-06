package wallgram.hd.wallpapers.util.download.manager

import android.app.Notification
import wallgram.hd.wallpapers.util.download.task.Task

object EmptyNotification : NotificationCreator {
    override fun init(task: Task) {

    }

    override fun create(task: Task, status: Status): Notification? {
        return null
    }
}