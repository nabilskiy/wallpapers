package wallgram.hd.wallpapers.presentation.subscribe

import wallgram.hd.wallpapers.data.gallery.GalleryCache

interface ChangeSubscription {

    fun changeSubscription(subscribed: Boolean): Boolean

    class Combo(
        private val changeSubscription: ChangeSubscription,
        private val communication: UpdateSubscriptions.Update
    ) : ChangeSubscription {

        override fun changeSubscription(subscribed: Boolean): Boolean {
            val newState = changeSubscription.changeSubscription(subscribed)
            communication.map(newState)
            return newState
        }
    }
}