package wallgram.hd.wallpapers.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.data.repository.DataRepositorySource
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
        private val dataRepository: DataRepositorySource
) : BaseViewModel() {

    private val categoriesLiveDataPrivate = MutableLiveData<Resource<List<Category>>>()
    val categoriesLiveData: LiveData<Resource<List<Category>>> get() = categoriesLiveDataPrivate

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

}