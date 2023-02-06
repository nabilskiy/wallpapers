package wallgram.hd.wallpapers.presentation.settings

import wallgram.hd.wallpapers.core.Read
import wallgram.hd.wallpapers.core.Save
import wallgram.hd.wallpapers.core.data.PreferenceDataStore

interface SettingsStore {

    interface Save : wallgram.hd.wallpapers.core.Save<Boolean>
    interface Read : wallgram.hd.wallpapers.core.Read<Boolean>

    interface Mutable : Save, Read

    class Base(private val preferences: PreferenceDataStore) : Mutable {

        override fun save(data: Boolean) = preferences.save(KEY, data)

        override fun read() = preferences.readBoolean(KEY, true)

        companion object {
            private const val KEY = "NotificationstKey"
        }
    }
}