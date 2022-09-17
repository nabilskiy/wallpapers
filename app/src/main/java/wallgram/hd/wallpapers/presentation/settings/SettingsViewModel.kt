package wallgram.hd.wallpapers.presentation.settings

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.data.settings.FileCacheSource
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val cacheSource: FileCacheSource,
    private val communication: SettingsCommunication,
    dispatchers: wallgram.hd.wallpapers.core.Dispatchers
    ) : BaseViewModel(dispatchers) {
    fun init() {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val folderSize = cacheSource.folderSize()
            val filesSize = cacheSource.fileSize(folderSize)

            val resultUi = SettingsUi.Base(
                resourceProvider.string(
                    R.string.cache_size,
                    filesSize
                )
            )

            withContext(Dispatchers.Main) { communication.map(resultUi) }
        }
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            cacheSource.clear()
            fetch()
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<SettingsUi>) =
        communication.observe(owner, observer)

}