package wallgram.hd.wallpapers.ui.wallpaper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.util.cache.CacheManager
import wallgram.hd.wallpapers.util.cache.ICacheManager
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

class WallpaperViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource,
    private val cacheManager: ICacheManager
) : BaseViewModel() {

    val wallpapersLiveData: MutableLiveData<PagingData<Gallery>> get() = cacheManager.wallpapersData

    private val favoriteLiveDataPrivate = MutableLiveData<Boolean>()
    val favoriteLiveData: LiveData<Boolean> get() = favoriteLiveDataPrivate


    fun onBack() {
        modo.back()
    }

    fun onCropClicked(item: Gallery) {
        modo.forward(Screens.Crop(item))
    }

    fun setFavorite(gallery: Gallery) {
        viewModelScope.launch {
            dataRepository.addToFavorites(gallery)
            dataRepository.isFavorite(gallery.id).collect{
                favoriteLiveDataPrivate.value = it
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

}