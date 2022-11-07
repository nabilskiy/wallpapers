package wallgram.hd.wallpapers.presentation.wallpaper

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.lang.ref.WeakReference

abstract class ContextAwareWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

     var weakContext: WeakReference<Context> = WeakReference(context)

}