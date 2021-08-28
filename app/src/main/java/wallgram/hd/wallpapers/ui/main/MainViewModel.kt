package wallgram.hd.wallpapers.ui.main

import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Category
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
) : BaseViewModel() {

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

            dataRepository.getSuggest(search, "ru").collect {
                suggestLiveDataPrivate.value = it
            }

        }
    }

    fun onItemClicked(it: Category, type: WallType) {
        modo.forward(Screens.CategoriesList(FeedRequest(type = type, category = it.id, categoryName = it.name)))
    }

    fun onItemClicked(color: Int, type: WallType) {
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        modo.forward(Screens.CategoriesList(FeedRequest(type = type, r = r.toString(), g = g.toString(), b = b.toString())))
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