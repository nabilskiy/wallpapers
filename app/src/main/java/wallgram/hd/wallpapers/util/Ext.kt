package wallgram.hd.wallpapers.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.webkit.URLUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.main.MainActivity
import wallgram.hd.wallpapers.util.localization.LocalizationActivityDelegate
import java.net.MalformedURLException

//fun Context.getResolution(): String{
//    val outMetrics = DisplayMetrics()
//   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) display?.getRealMetrics(outMetrics) else
//        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(outMetrics)
//
//    return "${outMetrics.widthPixels}x${outMetrics.heightPixels}"
//}

fun Activity.restart(){
    val KEY_ACTIVITY_LOCALE_CHANGED = "activity_locale_changed"
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra(KEY_ACTIVITY_LOCALE_CHANGED, true)
    this.startActivity(intent)
    this.finish()
    this.overridePendingTransition(0, 0)
}

fun Context.getAppName(): String {
    var name: String = try {
        (packageManager?.getApplicationLabel(applicationInfo) ?: "").toString()
    } catch (e: Exception) {
        ""
    }
    if (name.hasContent()) return name

    val stringRes = applicationInfo?.labelRes ?: 0
    name = if (stringRes == 0) {
        applicationInfo?.nonLocalizedLabel?.toString() ?: ""
    } else {
        getString(stringRes)
    }
    if (name.hasContent()) return name

    val def = getString(R.string.app_name)
    return if (def.hasContent()) def else "Unknown"
}

fun Context.getResolution(): String{
    val metrics = DisplayMetrics()
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    manager.defaultDisplay?.getMetrics(metrics)
    return metrics.widthPixels.toString() + "x" + metrics.heightPixels
}

fun isValidUrl(urlString: String): Boolean {
    try {
        return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
    } catch (ignored: MalformedURLException) {
        Log.d("dd", ignored.toString())
    }
    return false
}
