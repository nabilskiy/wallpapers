package wallgram.hd.wallpapers.presentation.settings.resolution

import wallgram.hd.wallpapers.core.presentation.Communication

interface UpdateResolutions {

    interface Observe : Communication.Observe<Boolean>
    interface Update : Communication.Update<Boolean>

    class Base : Communication.SingleUiUpdate<Boolean>(), Observe, Update
}