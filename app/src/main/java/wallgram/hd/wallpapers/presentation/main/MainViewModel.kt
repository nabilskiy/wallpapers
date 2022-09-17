package wallgram.hd.wallpapers.presentation.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    val messages: LiveData<Int>
        get() = billingRepository.messages.asLiveData()

    val billingLifecycleObserver: LifecycleObserver
        get() = billingRepository.billingLifecycleObserver

    init {
        getCategories()
    }

    fun getCategories() {
//        viewModelScope.launch {
//            categoriesLiveDataPrivate.value = Resource.Loading()
//
//
//        }
    }

    fun getSuggest(search: String) {
        viewModelScope.launch {


//            dataRepository.getSuggest(search).collect {
//                suggestLiveDataPrivate.value = it
//            }

        }
    }

    fun onItemClicked(it: FiltersCloud.Base, type: WallType) {
       // modo.forward(Screens.CategoriesList(FeedRequest(type = type, category = it.id, categoryName = it.name)))
    }

    fun onItemClicked(color: Int, title: String, type: WallType) {
//        val r = color shr 16 and 0xFF
//        val g = color shr 8 and 0xFF
//        val b = color shr 0 and 0xFF
//        modo.forward(Screens.CategoriesList(FeedRequest(type = type, categoryName = title, r = r.toString(), g = g.toString(), b = b.toString())))
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