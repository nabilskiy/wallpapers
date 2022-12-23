package wallgram.hd.wallpapers

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import javax.inject.Inject

interface ResourceProvider {

    fun string(@StringRes id: Int): String
    fun string(@StringRes id: Int, vararg args: Any): String

    fun color(@ColorRes id: Int): Int

    //fun getErrorMessage(exceptionType: ExceptionType): String


    class Base @Inject constructor(private var context: Context) : ResourceProvider {
        override fun string(id: Int) = context.getString(id)

        override fun string(id: Int, vararg args: Any) = context.getString(id, *args)
        override fun color(id: Int) = ContextCompat.getColor(context, id)


//        override fun getErrorMessage(exceptionType: ExceptionType) = string(
//            when (exceptionType) {
//                ExceptionType.NETWORK_UNAVAILABLE -> R.string.error_msg
//                else -> R.string.error
//            }
//        )

    }
}
