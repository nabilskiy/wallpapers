package wallgram.hd.wallpapers.presentation.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Delay
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.domain.gallery.GalleryInteractor
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.Refreshing
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.wallpapers.UpdateSave
import wallgram.hd.wallpapers.util.modo.externalForward
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val interactor: GalleryInteractor,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers), Search {

    private var lastVisibleItemPos = -1

    private val wallpapersLiveDataPrivate = MutableLiveData<GalleriesUi>()
    val wallpapersLiveData: MutableLiveData<GalleriesUi> get() = wallpapersLiveDataPrivate

    private val progressLiveDataPrivate = MutableLiveData<Refreshing>()
    val progressLiveData: LiveData<Refreshing> get() = progressLiveDataPrivate

    private val initial = GalleriesUi.Base(listOf(SearchUi()))

    private var request = WallpaperRequest.SEARCH("")

    private val delay = Delay<String> { query ->

        handle {
            find(query)
        }
    }

    private val atFinish = {
        progressLiveDataPrivate.value = Refreshing.Done()
    }

    private var cleared = false

    private suspend fun find(query: String) {

        request = request.copy(query)

        handle {
            interactor.gallery(request, atFinish) {
                it.map(object : Mapper.Unit<List<ItemUi>> {
                    override fun map(data: List<ItemUi>) {
                        val result = ArrayList<ItemUi>()
                        result.addAll(data)

                        if (!cleared)
                            wallpapersLiveDataPrivate.value = GalleriesUi.Base(result)
                    }
                })

            }
        }
    }

    fun reset() {
        request.initialPage()
    }

    override fun search(query: String) {
        cleared = query.length < 3
        if (cleared) {
            delay.clear()
            wallpapersLiveDataPrivate.value = initial
        } else {
            if (request.query() != query) {
                wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(ProgressUi()))

            }
            //wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(SearchEmptyUi()))
            delay.add(query.lowercase())
        }
    }

    fun loadMoreData(query: String, lastVisibleItemPosition: Int) {
        if (lastVisibleItemPosition != lastVisibleItemPos)
            if (interactor.needToLoadMoreData(request.itemId(), lastVisibleItemPosition)) {
                lastVisibleItemPos = lastVisibleItemPosition
                search(query)
            }
    }


    fun init() {
        wallpapersLiveDataPrivate.value = initial
    }
}