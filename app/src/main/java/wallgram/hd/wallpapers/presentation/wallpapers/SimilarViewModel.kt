package wallgram.hd.wallpapers.presentation.wallpapers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.base.*
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.NavigateGallery
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward

import javax.inject.Inject

@HiltViewModel
class SimilarViewModel @Inject constructor(
    private val interactor: GalleryInteractor,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private var lastVisibleItemPos = -1

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    private val progressLiveDataPrivate = MutableLiveData<Refreshing>()
    val progressLiveData: LiveData<Refreshing> get() = progressLiveDataPrivate

    init {
        wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(ProgressUi()))
    }

    fun fetch(wallpaperRequest: WallpaperRequest) {
        loadData(wallpaperRequest)

    }

    private val atFinish = {
        progressLiveDataPrivate.value = Refreshing.Done()
    }

    fun clear(request: WallpaperRequest) {
        interactor.clear(request)
    }

    fun loadMoreData(wallpaperRequest: WallpaperRequest, lastVisibleItemPosition: Int) {
        if (lastVisibleItemPosition != lastVisibleItemPos)
            if (interactor.needToLoadMoreData(wallpaperRequest.itemId(), lastVisibleItemPosition)) {
                lastVisibleItemPos = lastVisibleItemPosition
                loadData(wallpaperRequest)
            }
    }

    private fun loadData(wallpaperRequest: WallpaperRequest) {
        handle {
            delay(500)
            interactor.gallery(wallpaperRequest, atFinish) { wallpapersLiveDataPrivate.value = it }
        }
    }

}