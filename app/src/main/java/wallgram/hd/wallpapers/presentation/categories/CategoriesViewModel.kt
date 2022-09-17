package wallgram.hd.wallpapers.presentation.categories

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.filters.CategoriesInteractor
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.Refreshing
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val filtersInteractor: CategoriesInteractor,
    dispatchers: Dispatchers,
) : BaseViewModel(dispatchers) {

    private val categoriesLiveDataPrivate = MutableLiveData<FiltersUi>()
    val categoriesLiveData: LiveData<FiltersUi> get() = categoriesLiveDataPrivate

    private val progressLiveDataPrivate = MutableLiveData<Refreshing>()
    val progressLiveData: LiveData<Refreshing> get() = progressLiveDataPrivate

    init {
       loadData()
    }

    fun loadData(){
        categoriesLiveDataPrivate.value = FiltersUi.Base(listOf(ProgressUi()))
        handle {
            filtersInteractor.filters(atFinish) { categoriesLiveDataPrivate.value = it }
        }
    }

    private val atFinish = {
        progressLiveDataPrivate.value = Refreshing.Done()
    }


}