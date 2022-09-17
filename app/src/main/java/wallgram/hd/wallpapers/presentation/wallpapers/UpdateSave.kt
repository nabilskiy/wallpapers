package wallgram.hd.wallpapers.presentation.wallpapers

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface UpdateSave {

    interface Observe : Communication.Observe<Boolean>
    interface Update : Communication.Update<Boolean>

    class Base @Inject constructor() : Communication.SingleUiUpdate<Boolean>(), Observe, Update
}