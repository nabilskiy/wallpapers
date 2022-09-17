package wallgram.hd.wallpapers.presentation.search

import androidx.lifecycle.LifecycleOwner
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

    private val initial = GalleriesUi.Base(listOf(SearchUi()))

    private val delay = Delay<String> { query ->
        handle {
            find(query)
        }
    }

    private val atFinish = {

    }

    private var cleared = false

    private suspend fun find(query: String) {

        handle {
            interactor.gallery(WallpaperRequest.SEARCH(query), atFinish) {
                it.map(object : Mapper.Unit<List<ItemUi>> {
                    override fun map(data: List<ItemUi>) {
                        val result = ArrayList<ItemUi>()
                        if (data.size == 1 && data.first() is ProgressUi)
                            result.add(SearchEmptyUi())
                        else result.addAll(data)

                        if (!cleared)
                            wallpapersLiveDataPrivate.value = GalleriesUi.Base(result)
                    }
                })

            }
        }
    }

    fun itemClicked(wallpaperRequest: WallpaperRequest, position: Int) {
        interactor.save(wallpaperRequest, position)
        modo.externalForward(Screens.Wallpaper())
    }

    override fun search(query: String) {
        cleared = query.length < 3
        if (cleared) {
            delay.clear()
            wallpapersLiveDataPrivate.value = initial
        } else {
            wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(ProgressUi()))
            //wallpapersLiveDataPrivate.value = GalleriesUi.Base(listOf(SearchEmptyUi()))
            delay.add(query.lowercase())
        }
    }

    fun loadMoreData(query: String, lastVisibleItemPosition: Int) {
        if (lastVisibleItemPosition != lastVisibleItemPos)
            if (interactor.needToLoadMoreData(lastVisibleItemPosition)) {
                lastVisibleItemPos = lastVisibleItemPosition
                search(query)
            }
    }


    fun init() {
        wallpapersLiveDataPrivate.value = initial
    }
}