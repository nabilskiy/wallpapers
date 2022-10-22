package wallgram.hd.wallpapers.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.domain.home.HomeInteractor
import wallgram.hd.wallpapers.presentation.base.*
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractor,
    dispatchers: Dispatchers,
) : BaseViewModel(dispatchers) {

    private val categoriesLiveDataPrivate = MutableLiveData<FiltersUi>()
    val categoriesLiveData: LiveData<FiltersUi> get() = categoriesLiveDataPrivate

    private val progressLiveDataPrivate = MutableLiveData<Refreshing>()
    val progressLiveData: LiveData<Refreshing> get() = progressLiveDataPrivate


    init {
        loadData()
    }

    fun loadData() {
        categoriesLiveDataPrivate.value = FiltersUi.Base(listOf(ProgressUi()))
        handle {
            homeInteractor.filters({ atFinish.invoke() }) { categoriesLiveDataPrivate.value = it }
        }
    }

    private val atFinish = {
        progressLiveDataPrivate.value = Refreshing.Done()
    }

    fun search() {
        showScreen(Screens.Search())
    }

    fun navigateSubscriptions() {
        showScreen(Screens.Subscription())
    }


}