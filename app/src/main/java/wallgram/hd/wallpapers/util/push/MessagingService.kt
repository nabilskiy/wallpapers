package wallgram.hd.wallpapers.util.push

import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.graphics.BitmapFactory
import wallgram.hd.wallpapers.R

class MessagingService : FirebaseMessagingService() {

        override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}