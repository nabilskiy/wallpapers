package wallgram.hd.wallpapers.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//fun Context.getResolution(): String{
//    val outMetrics = DisplayMetrics()
//   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) display?.getRealMetrics(outMetrics) else
//        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(outMetrics)
//
//    return "${outMetrics.widthPixels}x${outMetrics.heightPixels}"
//}

fun Context.getResolution(): String{
    val metrics = DisplayMetrics()
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    manager.defaultDisplay?.getMetrics(metrics)
    return metrics.widthPixels.toString() + "x" + metrics.heightPixels
}
