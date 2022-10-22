package wallgram.hd.wallpapers.presentation.main

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface MainCommunication : Communication.Mutable<FirstLaunchUi> {
    class Base @Inject constructor() : Communication.UiUpdate<FirstLaunchUi>(), MainCommunication
}
