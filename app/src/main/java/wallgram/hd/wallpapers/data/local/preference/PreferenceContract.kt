package wallgram.hd.wallpapers.data.local.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import wallgram.hd.wallpapers.PREFERENCES

const val FIRST_LAUNCH = "first_launch"
const val LANGUAGE = "language"

const val TIMER_VISIBILITY_PREFS = "timer_visibility"
const val LIFE_EXPECTANCY_PREFS = "life_expectancy"
const val LAST_LAUNCH_DATE = "last_launch_date"

interface PreferenceContract {
    fun save(key: String, value: String)
    fun save(key: String, value: Boolean)
    fun save(key: String, value: Int)
    fun save(key: String, value: Long)

    fun getString(key: String, defaultValue: String = ""): String
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun getLong(key: String, defaultValue: Long = 0): Long
}

class SharedPreferencesImpl(context: Context) : PreferenceContract {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)

    override fun save(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getString(key: String, defaultValue: String) =
        sharedPreferences.getString(key, defaultValue).orEmpty()

    override fun save(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    override fun save(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    override fun save(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun getLong(key: String, defaultValue: Long): Long =
        sharedPreferences.getLong(key, defaultValue)

}