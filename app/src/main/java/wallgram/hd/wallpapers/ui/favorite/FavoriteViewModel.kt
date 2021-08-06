package wallgram.hd.wallpapers.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.remote.ServiceGenerator
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.model.ServerResponse
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.util.cache.ICacheManager
import wallgram.hd.wallpapers.util.modo.externalForward
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val serviceGenerator: ServiceGenerator,
    private val dataRepository: DataRepositorySource,
    private val cacheManager: ICacheManager
) : BaseViewModel() {

    private val tagsLiveDataPrivate = MutableLiveData<Resource<ServerResponse<Tag>>>()
    val tagsLiveData: LiveData<Resource<ServerResponse<Tag>>> get() = tagsLiveDataPrivate

    private val popularLiveDataPrivate = MutableLiveData<Resource<ServerResponse<Gallery>>>()
    val popularLiveData: LiveData<Resource<ServerResponse<Gallery>>> get() = popularLiveDataPrivate

    private val favoritesLiveDataPrivate = MutableLiveData<Resource<List<Gallery>>>()
    val favoritesLiveData: LiveData<Resource<List<Gallery>>> get() = favoritesLiveDataPrivate

    private val categoriesLiveDataPrivate = MutableLiveData<Resource<List<Category>>>()
    val categoriesLiveData: LiveData<Resource<List<Category>>> get() = categoriesLiveDataPrivate

//    fun getFlow(category: Int, sort: String): Flow<PagingData<Gallery>> {
//        return Pager(
//                PagingConfig(27, enablePlaceholders = false)
//        ) {
//            FeedPagingSource(FeedRequest(category = category, sort = sort), serviceGenerator)
//        }.flow.cachedIn(viewModelScope)
//    }
//
//    fun getFlow(sort: String): Flow<PagingData<Gallery>> {
//        return Pager(
//                PagingConfig(27, enablePlaceholders = false)
//        ) {
//            FeedPagingSource(FeedRequest(sort = sort), serviceGenerator)
//        }.flow.cachedIn(viewModelScope)
//    }

    fun getCategories() {
        viewModelScope.launch {
            categoriesLiveDataPrivate.value = Resource.Loading()

            dataRepository.getCategories().collect {
                categoriesLiveDataPrivate.value = it
            }

        }
    }

    fun getTags() {
        viewModelScope.launch {
            tagsLiveDataPrivate.value = Resource.Loading()

            dataRepository.getTags(1).collect {
                tagsLiveDataPrivate.value = it
            }

        }
    }

    fun getFavorites(type: Int) {
        viewModelScope.launch {
            favoritesLiveDataPrivate.value = Resource.Loading()

            dataRepository.getSavedItems(type).collect {
                favoritesLiveDataPrivate.value = Resource.Success(it)
            }

        }
    }

    fun getPopular() {
        viewModelScope.launch {
            popularLiveDataPrivate.value = Resource.Loading()

            dataRepository.getWallpapers("popular").collect {
                popularLiveDataPrivate.value = it
            }

        }
    }

    fun deleteItem(item: Gallery) {
        viewModelScope.launch {
            dataRepository.deleteItem(item)
        }
    }

    fun clearAll(type: Int) {
        viewModelScope.launch {
            dataRepository.deleteAllItems(type)
        }
    }

    fun itemClicked(position: Int, id: Int) {
        favoritesLiveDataPrivate.value?.let {
            val data = it.data
            if (data.isNullOrEmpty())
                return

            cacheManager.wallpapersData = MutableLiveData(PagingData.from(data))
            modo.externalForward(Screens.Wallpaper(position, id))
        }
    }
}