package wallgram.hd.wallpapers.ui.settings

import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.util.modo.back
import javax.inject.Inject

class SettingsViewModel @Inject constructor(preferences: PreferenceContract) : BaseViewModel() {
    fun onBackClicked() {
        modo.back()
    }
}