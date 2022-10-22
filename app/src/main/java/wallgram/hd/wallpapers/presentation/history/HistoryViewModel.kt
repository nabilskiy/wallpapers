package wallgram.hd.wallpapers.presentation.history

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.history.HistoryInteractor
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.util.modo.externalForward
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val interactor: HistoryInteractor,
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
        handle {
            interactor.history(atFinish) { wallpapersLiveDataPrivate.value = it }
        }
    }
    
    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Boolean>) =
        update.observe(owner, observer)

    private val atFinish = {

    }

}