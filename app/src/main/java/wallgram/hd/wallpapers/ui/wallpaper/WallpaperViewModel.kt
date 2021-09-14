package wallgram.hd.wallpapers.ui.wallpaper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.paging.FeedPagingSource
import wallgram.hd.wallpapers.data.remote.ServiceGenerator
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.Pic
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.cache.CacheManager
import wallgram.hd.wallpapers.util.cache.ICacheManager
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

class WallpaperViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource,
    private val serviceGenerator: ServiceGenerator,
    private val cacheManager: ICacheManager
) : BaseViewModel() {

    val wallpapersLiveData: MutableLiveData<PagingData<Gallery>> get() = cacheManager.wallpapersData

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
                FeedPagingSource(feedRequest, serviceGenerator)
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

    fun getPic(id: Int, res: String){
        picLiveDataPrivate.value = Resource.Loading()
        viewModelScope.launch {
            dataRepository.getPic(id, res, "ru").collect{
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

    fun itemClicked(position: Int, id: Int) {
        cacheManager.wallpapersData = similarLiveDataPrivate
        modo.externalForward(Screens.Wallpaper(position, id))
    }

    fun clearInformation() {
        picLiveDataPrivate.value = Resource.DataError(0)
    }

    fun onTagClicked(tag: Tag) {
        modo.forward(Screens.CategoriesList(FeedRequest(type = WallType.TAG, category = tag.id, categoryName = tag.name)))
    }

}