package wallgram.hd.wallpapers.presentation.wallpapers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.domain.gallery.SimilarInteractor
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.base.ToolbarUi
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.NavigateGallery
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward

import javax.inject.Inject

@HiltViewModel
class SimilarViewModel @Inject constructor(
    private val interactor: SimilarInteractor,
    private val resourceProvider: ResourceProvider,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private var lastVisibleItemPos = -1

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    init {
        wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(ProgressUi()))
    }

    fun fetch(wallpaperRequest: WallpaperRequest) {
        loadData(wallpaperRequest)

    }

    private val atFinish = {

    }

    fun save(request: WallpaperRequest) {
        interactor.save(request, 0)
    }

    fun itemClicked(wallpaperRequest: WallpaperRequest, position: Int) {
        interactor.save(wallpaperRequest, position)
        modo.externalForward(Screens.Wallpaper())
    }

    fun loadMoreData(wallpaperRequest: WallpaperRequest, lastVisibleItemPosition: Int) {
        if (lastVisibleItemPosition != lastVisibleItemPos)
            if (interactor.needToLoadMoreData(lastVisibleItemPosition)) {
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