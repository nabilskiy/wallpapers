package wallgram.hd.wallpapers.ui.wallpaper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.ui.base.Screens
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.paging.FeedPagingSource
import wallgram.hd.wallpapers.data.remote.ServiceGenerator
import wallgram.hd.wallpapers.data.repository.billing.BillingRepository
import wallgram.hd.wallpapers.data.repository.data.DataRepositorySource
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.Pic
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.DisplayHelper
import wallgram.hd.wallpapers.util.cache.ICacheManager
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

class WallpaperViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource,
    private val serviceGenerator: ServiceGenerator,
    private val cacheManager: ICacheManager,
    private val localizationDelegate: LocalizationApplicationDelegate,
    private val displayHelper: DisplayHelper,
    private val billingRepository: BillingRepository
) : BaseViewModel() {

    val wallpapersLiveData: MutableLiveData<PagingData<Gallery>> get() = cacheManager.wallpapersData
    val similarData: MutableLiveData<PagingData<Gallery>> get() = cacheManager.similarData

    private val similarLiveDataPrivate = MutableLiveData<PagingData<Gallery>>()
    val similarLiveData: MutableLiveData<PagingData<Gallery>> get() = similarLiveDataPrivate

    private val favoriteLiveDataPrivate = MutableLiveData<Boolean>()
    val favoriteLiveData: LiveData<Boolean> get() = favoriteLiveDataPrivate

    private val picLiveDataPrivate = MutableLiveData<Resource<Pic>>()
    val picLiveData: LiveData<Resource<Pic>> get() = picLiveDataPrivate

    fun onBack() {
        modo.back()
    }

    fun onCropClicked(landscape: String) {
        modo.forward(Screens.Crop(landscape))
    }

    fun getLiveData(feedRequest: FeedRequest) {
        viewModelScope.launch {
            Pager(PagingConfig(27, enablePlaceholders = false)) {
                FeedPagingSource(feedRequest, serviceGenerator, localizationDelegate, displayHelper)
            }.flow.cachedIn(viewModelScope).collect {
                similarLiveDataPrivate.value = it
            }
        }
    }


    fun setFavorite(gallery: Gallery) {
        viewModelScope.launch {
            gallery.type = 0
            dataRepository.addToFavorites(gallery)
            dataRepository.isFavorite(gallery.id).collect{
                favoriteLiveDataPrivate.value = it
            }
        }
    }

    fun addToHistory(gallery: Gallery){
        viewModelScope.launch {
            gallery.type = 1
            dataRepository.addToFavorites(gallery)
        }
    }

    fun getPic(id: Int, res: String){
        picLiveDataPrivate.value = Resource.Loading()
        viewModelScope.launch {
            dataRepository.getPic(id, res).collect{
                picLiveDataPrivate.value = it
            }
        }
    }

    fun isFavorite(gallery: Gallery){
        viewModelScope.launch {

            dataRepository.isFavorite(gallery.id).collect{
                favoriteLiveDataPrivate.value = it
            }
        }
    }

    fun getCurrentSub(): LiveData<String>{
        return billingRepository.getCurrentSub().asLiveData()
    }

    fun itemClicked(position: Int, id: Int) {
        cacheManager.similarData = similarLiveDataPrivate
        modo.forward(Screens.Wallpaper(position, id, WallType.SIMILAR))
    }

    fun clearInformation() {
        picLiveDataPrivate.value = Resource.DataError(0)
    }

    fun onTagClicked(tag: Tag) {
        modo.forward(Screens.CategoriesList(FeedRequest(type = WallType.TAG, category = tag.id, categoryName = tag.name)))
    }

}