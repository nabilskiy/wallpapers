package wallgram.hd.wallpapers.presentation.start

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface StartCommunication : Communication.Mutable<StartUi> {
    class Base @Inject constructor() : Communication.UiUpdate<StartUi>(), StartCommunication
}
