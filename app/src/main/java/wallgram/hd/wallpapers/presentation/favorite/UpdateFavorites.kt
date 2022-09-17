package wallgram.hd.wallpapers.presentation.favorite

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface UpdateFavorites {

    interface Observe : Communication.Observe<Boolean>
    interface Update : Communication.Update<Boolean>

    class Base @Inject constructor() : Communication.UiUpdate<Boolean>(), Observe, Update
}