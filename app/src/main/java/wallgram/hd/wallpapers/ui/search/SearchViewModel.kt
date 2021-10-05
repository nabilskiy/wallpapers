package wallgram.hd.wallpapers.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.paging.FeedPagingSource
import wallgram.hd.wallpapers.data.remote.ServiceGenerator
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.util.cache.ICacheManager
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import wallgram.hd.wallpapers.util.modo.externalForward
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val dataRepository: DataRepositorySource,
        private val serviceGenerator: ServiceGenerator,
        private val cacheManager: ICacheManager,
        private val languageDelegate: LocalizationApplicationDelegate
) : BaseViewModel() {
    private val suggestLiveDataPrivate = MutableLiveData<Resource<List<String>>>()
    val suggestLiveData: LiveData<Resource<List<String>>> get() = suggestLiveDataPrivate

    private val searchLiveDataPrivate = MutableLiveData<PagingData<Gallery>>()
    val searchLiveData: LiveData<PagingData<Gallery>> get() = searchLiveDataPrivate

    fun getSuggest(search: String) {
        viewModelScope.launch {
            suggestLiveDataPrivate.value = Resource.Loading()

            dataRepository.getSuggest(search).collect {
                suggestLiveDataPrivate.value = it
            }
        }
    }

    fun getFlow(search: String): Flow<PagingData<Gallery>> {
        val p = Pager(
                PagingConfig(27, enablePlaceholders = false)
        ) {
            FeedPagingSource(FeedRequest(search = search, type = WallType.SEARCH), serviceGenerator, languageDelegate)
        }.flow.cachedIn(viewModelScope)

        viewModelScope.launch {
            p.collect {
                searchLiveDataPrivate.value = it
            }
        }

        return p
    }

    fun onItemClicked(position: Int, id: Int) {
        cacheManager.wallpapersData = searchLiveDataPrivate
        modo.externalForward(Screens.Wallpaper(position, id))
    }
}