package wallgram.hd.wallpapers.presentation.wallpaper

import wallgram.hd.wallpapers.core.Read
import wallgram.hd.wallpapers.core.Save
import wallgram.hd.wallpapers.core.data.PreferenceDataStore

interface DownloadsCountStore {

    interface Save : wallgram.hd.wallpapers.core.Save<Int>
    interface Read : wallgram.hd.wallpapers.core.Read<Int>

    interface Mutable : Save, Read

    class Base(private val preferences: PreferenceDataStore) : Mutable {

        override fun save(data: Int) = preferences.save(KEY, data)

        override fun read() = preferences.readInt(KEY)

        companion object {
            private const val KEY = "DownloadsCountKey"
        }
    }
}