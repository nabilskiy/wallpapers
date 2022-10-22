package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.core.data.PreferenceDataStore

interface ResolutionsCache {

    interface Save : wallgram.hd.wallpapers.core.Save<String>
    interface Read : wallgram.hd.wallpapers.core.Read<String>

    interface Mutable : Save, Read

    class Base(private val preferences: PreferenceDataStore) : Mutable {

        override fun save(data: String) = preferences.save(KEY, data)

        override fun read() = preferences.readString(KEY)

        companion object {
            private const val KEY = "SelectedResolutionKey"
        }
    }
}