package wallgram.hd.wallpapers.presentation.favorite

import wallgram.hd.wallpapers.data.gallery.GalleryCache

interface ChangeFavorite {

    fun changeFavorite(item: GalleryCache.Base): Boolean

    class Combo(
        private val changeFavorite: ChangeFavorite,
        private val communication: UpdateFavorites.Update
    ) : ChangeFavorite {

        override fun changeFavorite(item: GalleryCache.Base): Boolean {
            val newState = changeFavorite.changeFavorite(item)
            communication.map(newState)
            return newState
        }
    }
}