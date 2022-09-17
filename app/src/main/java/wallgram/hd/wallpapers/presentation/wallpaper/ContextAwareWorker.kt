package wallgram.hd.wallpapers.presentation.wallpaper

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.lang.ref.WeakReference

abstract class ContextAwareWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    private var weakContext: WeakReference<Context?>? = null
    val context: Context?
        get() = weakContext?.get()

    init {
        weakContext = WeakReference(context)
    }
}