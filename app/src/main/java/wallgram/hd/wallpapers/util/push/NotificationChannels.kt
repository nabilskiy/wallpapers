package wallgram.hd.wallpapers.util.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

object NotificationChannels{

    val URGENT_CHANNEL_ID = "urgent"
    val NORMAL_CHANNEL_ID = "normal"

    fun create(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createUrgentChannel(context)
            createNormalChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUrgentChannel(context: Context){
        val name = "Urgent Message"
        val channelDescription = "Urgent message"
        val priority = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(URGENT_CHANNEL_ID,name,priority).apply {
            description = channelDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNormalChannel(context: Context){
        val name = "Normal Message"
        val channelDescription = "Normal message"
        val priority = NotificationManager.IMPORTANCE_LOW

        val channel = NotificationChannel(NORMAL_CHANNEL_ID,name,priority).apply {
            description = channelDescription
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

}