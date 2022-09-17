package wallgram.hd.wallpapers.core.data

import android.content.SharedPreferences

interface PreferenceDataStore {

    fun save(key: String, data: Set<String>)
    fun save(key: String, data: Int)

    fun read(key: String): Set<String>
    fun readInt(key: String): Int

    class Base(private val sharedPreferences: SharedPreferences) : PreferenceDataStore {

        override fun save(key: String, data: Set<String>) =
            sharedPreferences.edit().putStringSet(key, data).apply()

        override fun save(key: String, data: Int) =
            sharedPreferences.edit().putInt(key, data).apply()

        override fun read(key: String): Set<String> =
            sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()

        override fun readInt(key: String): Int = sharedPreferences.getInt(key, 0)

    }
}