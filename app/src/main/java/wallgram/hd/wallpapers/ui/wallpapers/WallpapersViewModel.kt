package wallgram.hd.wallpapers.ui.wallpapers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import wallgram.hd.wallpapers.data.paging.FeedPagingSource
import wallgram.hd.wallpapers.data.remote.ServiceGenerator
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.util.cache.ICacheManager
import wallgram.hd.wallpapers.util.modo.externalForward

import javax.inject.Inject

class WallpapersViewModel @Inject constructor(
        private val serviceGenerator: ServiceGenerator,
        private val cacheManager: ICacheManager
) : BaseViewModel() {

    private val wallpapersLiveDataPrivate = MutableLiveData<PagingData<Gallery>>()
    val wallpapersLiveData: MutableLiveData<PagingData<Gallery>> get() = wallpapersLiveDataPrivate

    val message = MutableLiveData<String>()

    fun sendMessage(text: String) {
        message.value = text
    }

    fun getLiveData(type: WallType, category: Int, sort: String, resolution: String) {
        viewModelScope.launch {
            Pager(PagingConfig(27, enablePlaceholders = false)) {
                FeedPagingSource(FeedRequest(type = type, category = category, sort = sort, resolution = resolution), serviceGenerator)
            }.flow.cachedIn(viewModelScope).collect {
                 wallpapersLiveDataPrivate.value = it

            }
        }
    }

    fun itemClicked(position: Int, id: Int) {
        cacheManager.wallpapersData = wallpapersLiveDataPrivate
        modo.externalForward(Screens.Wallpaper(position, id))
    }

}