package wallgram.hd.wallpapers.presentation.wallpapers

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.Refreshing
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.NavigateGallery
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward

import javax.inject.Inject

@HiltViewModel
class WallpapersViewModel @Inject constructor(
    private val interactor: GalleryInteractor,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private var isLoading = false

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    private val progressLiveDataPrivate = MutableLiveData<Refreshing>()
    val progressLiveData: LiveData<Refreshing> get() = progressLiveDataPrivate

    fun fetch(wallpaperRequest: WallpaperRequest) {
        wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(ProgressUi()))
        loadData(wallpaperRequest)
    }

    fun clear(request: WallpaperRequest) {
        interactor.clear(request)
    }

    private val atFinish = {
        progressLiveDataPrivate.value = Refreshing.Done()
    }

    fun loadMoreData(wallpaperRequest: WallpaperRequest) {
        if (!isLoading) {
            loadData(wallpaperRequest)
            isLoading = true
        }
    }


    private fun loadData(wallpaperRequest: WallpaperRequest) {
        isLoading = true
        handle {
            interactor.gallery(wallpaperRequest, atFinish) {
                wallpapersLiveDataPrivate.value = it
                isLoading = false
            }
        }
    }

}