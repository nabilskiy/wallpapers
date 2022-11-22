package wallgram.hd.wallpapers.core.data

import android.content.SharedPreferences

interface PreferenceDataStore {

    fun save(key: String, data: Set<String>)
    fun save(key: String, data: Int)
    fun save(key: String, data: Boolean)
    fun save(key: String, data: String)

    fun read(key: String): Set<String>
    fun readInt(key: String): Int
    fun readString(key: String): String
    fun readBoolean(key: String, default: Boolean): Boolean

    class Base(private val sharedPreferences: SharedPreferences) : PreferenceDataStore {

        override fun save(key: String, data: Set<String>) =
            sharedPreferences.edit().putStringSet(key, data).apply()

        override fun save(key: String, data: Int) =
            sharedPreferences.edit().putInt(key, data).apply()

        override fun save(key: String, data: Boolean) =
            sharedPreferences.edit().putBoolean(key, data).apply()

        override fun save(key: String, data: String) =
            sharedPreferences.edit().putString(key, data).apply()

        override fun read(key: String): Set<String> =
            sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()

        override fun readBoolean(key: String, default: Boolean): Boolean =
            sharedPreferences.getBoolean(key, default)

        override fun readInt(key: String): Int = sharedPreferences.getInt(key, 0)

        override fun readString(key: String): String = sharedPreferences.getString(key, "") ?: ""

    }
}