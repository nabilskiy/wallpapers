package wallgram.hd.wallpapers.ui.main

import androidx.lifecycle.*
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.repository.data.DataRepositorySource
import wallgram.hd.wallpapers.model.Category
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.ui.base.Screens
import wallgram.hd.wallpapers.data.repository.billing.BillingRepository
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource,
    private val billingRepository: BillingRepository
) : BaseViewModel() {

    val messages: LiveData<Int>
        get() = billingRepository.messages.asLiveData()

    val billingLifecycleObserver: LifecycleObserver
        get() = billingRepository.billingLifecycleObserver

    private val categoriesLiveDataPrivate = MutableLiveData<Resource<List<Category>>>()
    val categoriesLiveData: LiveData<Resource<List<Category>>> get() = categoriesLiveDataPrivate

    private val suggestLiveDataPrivate = MutableLiveData<Resource<List<String>>>()
    val suggestLiveData: LiveData<Resource<List<String>>> get() = suggestLiveDataPrivate

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            categoriesLiveDataPrivate.value = Resource.Loading()

            dataRepository.getCategories().collect {
                categoriesLiveDataPrivate.value = it
            }

        }
    }

    fun getSuggest(search: String) {
        viewModelScope.launch {
            suggestLiveDataPrivate.value = Resource.Loading()

            dataRepository.getSuggest(search).collect {
                suggestLiveDataPrivate.value = it
            }

        }
    }

    fun onItemClicked(it: Category, type: WallType) {
        modo.forward(Screens.CategoriesList(FeedRequest(type = type, category = it.id, categoryName = it.name)))
    }

    fun onItemClicked(color: Int, title: String, type: WallType) {
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        modo.forward(Screens.CategoriesList(FeedRequest(type = type, categoryName = title, r = r.toString(), g = g.toString(), b = b.toString())))
    }

    fun onAllCategoryClicked() {
        modo.selectStack(1)
    }

    fun onSearchClicked() {
        modo.selectStack(3)
    }

    fun onItemClicked(screen: AppScreen) {
        if (screen is Screens.History) {
            modo.selectStack(2)
            return
        }
        modo.forward(screen)
    }

    fun onItemClicked(screen: ExternalScreen) {
        modo.launch(screen)
    }


}