package wallgram.hd.wallpapers.util.download.manager

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import wallgram.hd.wallpapers.util.download.claritypotion.ClarityPotion
import wallgram.hd.wallpapers.util.download.task.Task

private val notificationManager by lazy {
    ClarityPotion.clarityPotion.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

fun showNotification(task: Task, notification: Notification?) {
    notification?.let {
        notificationManager.notify(task.hashCode(), it)
    }
}

fun cancelNotification(task: Task) {
    notificationManager.cancel(task.hashCode())
}