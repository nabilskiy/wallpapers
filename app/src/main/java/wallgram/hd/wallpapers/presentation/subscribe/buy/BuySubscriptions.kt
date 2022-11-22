package wallgram.hd.wallpapers.presentation.subscribe.buy

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject

interface BuySubscriptions {

    interface Observe : Communication.Observe<String>
    interface Update : Communication.Update<String>

    class Base @Inject constructor() : Communication.SingleUiUpdate<String>(), Observe, Update
}