package wallgram.hd.wallpapers.ui.main

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
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
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.forward
import wallgram.hd.wallpapers.util.modo.selectStack
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val dataRepository: DataRepositorySource
): BaseViewModel(){

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
        modo.forward(Screens.CategoriesList(it, type = type))
    }


    fun onItemClicked(@ColorInt color: Int, type: WallType) {
       // modo.forward()
       // modo.forward(Screens.CategoriesList(it, type = type))
    }

    fun onAllCategoryClicked() {
        modo.selectStack(1)
    }

    fun onSearchClicked() {
        modo.selectStack(4)
    }


}