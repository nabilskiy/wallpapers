package wallgram.hd.wallpapers.presentation.wallpaper

import android.util.Log
import androidx.lifecycle.*
import androidx.work.WorkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.data.workers.DownloadManager
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.model.Pic
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.util.modo.back
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val interactor: GalleryInteractor,
    private val billingRepository: BillingRepository,
    private val update: UpdateFavorites.Observe,
    private val downloadManager: DownloadManager,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    private val positionLiveDataPrivate = MutableLiveData<Int>()
    val positionLiveData: MutableLiveData<Int> get() = positionLiveDataPrivate

    private var lastVisibleItemPos = -1

//    private val similarLiveDataPrivate = MutableLiveData<PagingData<Gallery>>()
//    val similarLiveData: MutableLiveData<PagingData<Gallery>> get() = similarLiveDataPrivate

    private val picLiveDataPrivate = MutableLiveData<Pic>()
    val picLiveData: LiveData<Pic> get() = picLiveDataPrivate

    fun clear() {
        interactor.clear()
    }

    override fun back() {
        clear()
        super.back()
    }

    fun init() {
        val data = interactor.read()
        wallpapersLiveDataPrivate.value = data.first as GalleriesUi
        positionLiveDataPrivate.value = data.second as Int
    }

    fun update() {
        wallpapersLiveDataPrivate.value = interactor.read().first as GalleriesUi
    }

    fun download(id: Int): LiveData<WorkInfo> {
        val data = interactor.read()
        val url = data.first.map(GalleriesUi.Mapper.Link(id))
        return downloadManager.download(url)
    }

    fun getCurrentSub(): LiveData<String> {
        return billingRepository.getCurrentSub().asLiveData()
    }

    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Boolean>) =
        update.observe(owner, observer)

    fun itemClicked(position: Int, id: Int) {
        // cacheManager.similarData = similarLiveDataPrivate
        //modo.forward(Screens.Wallpaper(position, id, WallType.SIMILAR))
    }


    fun onTagClicked(tag: Tag) {
        //modo.forward(Screens.CategoriesList(FeedRequest(type = WallType.TAG, category = tag.id, categoryName = tag.name)))
    }

    fun changeFavorite(currentItem: ItemUi) {
        currentItem.changeFavorite()
    }

    fun cancelWorkManagerTasks() {
        downloadManager.cancelWorkManagerTasks()
    }

    fun install(i: Int, id: Int): LiveData<WorkInfo> {
        val data = interactor.read()
        val url = data.first.map(GalleriesUi.Mapper.Link(id))
        return downloadManager.apply(url, i)
    }

    fun loadMoreData(lastVisibleItemPosition: Int) {
        val request = interactor.read().third
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
        val request = interactor.read().third
        handle {
            interactor.gallery(request, atFinish) { wallpapersLiveDataPrivate.value = it }
        }
    }

}