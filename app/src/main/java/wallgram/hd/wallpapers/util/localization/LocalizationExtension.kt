package wallgram.hd.wallpapers.util.localization

import android.content.Context
import wallgram.hd.wallpapers.util.localization.LocalizationUtility

fun Context.toLocalizedContext(): Context = LocalizationUtility.getLocalizedContext(this)
