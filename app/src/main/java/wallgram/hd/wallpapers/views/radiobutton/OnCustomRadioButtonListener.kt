package wallgram.hd.wallpapers.views.radiobutton

import android.view.View
import wallgram.hd.wallpapers.presentation.subscribe.Subscription

interface OnCustomRadioButtonListener {
    fun onClick(subscription: Subscription)
}