package wallgram.hd.wallpapers.presentation.wallpaper

import androidx.lifecycle.*
import androidx.work.WorkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.data.workers.DownloadManager
import wallgram.hd.wallpapers.data.workers.WallpaperDownloader
import wallgram.hd.wallpapers.di.qualifiers.CarouselMapper
import wallgram.hd.wallpapers.domain.gallery.GalleriesDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val interactor: GalleryInteractor,
    @CarouselMapper private val mapper: GalleriesDomain.Mapper<GalleriesUi>,
    private val update: UpdateFavorites.Observe,
    private val downloadManager: DownloadManager,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    private val positionLiveDataPrivate = MutableLiveData<Int>()
    val positionLiveData: MutableLiveData<Int> get() = positionLiveDataPrivate

    private var lastVisibleItemPos = -1

    private val downloadLiveDataPrivate = MutableLiveData<WallpaperDownloader.Result>()
    val downloadLiveData: LiveData<WallpaperDownloader.Result> get() = downloadLiveDataPrivate

    fun clear() {
        interactor.clear()
    }

    override fun back() {
        clear()
        super.back()
    }

    fun init() {
        fetchWallpapers()
        fetchPosition()
    }

    private fun fetchPosition() {
        val cache = interactor.read()
        val position = cache.first.map(mapper).map(GalleriesUi.Mapper.Position(cache.second))
        positionLiveDataPrivate.value = position
    }

    fun fetchWallpapers() {
        val cache = interactor.read()
        val data = cache.first
        val mapped = data.map(mapper)

        wallpapersLiveDataPrivate.value = mapped
    }

    fun download(id: Int) {
        val data = interactor.read()
        val url = data.first.map(GalleriesDomain.Mapper.Link(id))

        val result = downloadManager.download(url)

        downloadLiveDataPrivate.postValue(result)
    }

    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Boolean>) =
        update.observe(owner, observer)

    fun changeFavorite(currentItem: ItemUi) {
        currentItem.changeFavorite()
    }

    fun cancelWorkManagerTasks() {
        downloadManager.cancelWorkManagerTasks()
    }

    fun cancelDownload(id: Long) {
        downloadManager.cancel(id)
        //downloadLiveDataPrivate.postValue(WallpaperDownloader.Result.Failure())
    }

    fun install(i: Int, id: Int): LiveData<WorkInfo> {
        val data = interactor.read()
        val url = data.first.map(GalleriesDomain.Mapper.Link(id))
        return downloadManager.apply(url, i)
    }

    fun loadMoreData(lastVisibleItemPosition: Int) {
        val request = interactor.read().third.apply {
            amount(20)
        }
        if (request.needToLoadMore())
            if (lastVisibleItemPosition != lastVisibleItemPos) {
                if (interactor.needToLoadMoreData(request.itemId(), lastVisibleItemPosition)) {
                    lastVisibleItemPos = lastVisibleItemPosition
                    loadData()
                }
            }
    }


    private val atFinish = {

    }

    private fun loadData() {
        val request = interactor.read().third.apply {
            amount(20)
        }
        handle {
            interactor.gallery(request, atFinish) { wallpapersLiveDataPrivate.value = it }
        }
    }

}