package wallgram.hd.wallpapers.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
        waitMs: Long = 300L,
        scope: CoroutineScope,
        destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun Context.getResolution(): String{
    val outMetrics = DisplayMetrics()
   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) display?.getRealMetrics(outMetrics) else
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(outMetrics)

    return "${outMetrics.widthPixels}x${outMetrics.heightPixels}"
}
