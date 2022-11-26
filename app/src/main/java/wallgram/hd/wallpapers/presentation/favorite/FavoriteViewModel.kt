package wallgram.hd.wallpapers.presentation.favorite

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.favorites.FavoritesInteractor
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.wallpapers.UpdateSave
import wallgram.hd.wallpapers.util.modo.externalForward
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val interactor: FavoritesInteractor,
    private val update: UpdateFavorites.Observe,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    init {
        fetch()
    }

    fun fetch() {
        wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(ProgressUi()))
        loadData()
    }

    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Boolean>) =
        update.observe(owner, observer)

    private fun loadData() {
        handle {
            interactor.favorites({

            }) { wallpapersLiveDataPrivate.value = it }
        }
    }


    private val atFinish = {

    }

}