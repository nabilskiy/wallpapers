package wallgram.hd.wallpapers.presentation.settings

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface SettingsCommunication : Communication.Mutable<SettingsUi> {
    class Base @Inject constructor() : Communication.UiUpdate<SettingsUi>(), SettingsCommunication
}
