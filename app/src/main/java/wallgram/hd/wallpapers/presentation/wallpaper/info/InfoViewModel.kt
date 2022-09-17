package wallgram.hd.wallpapers.presentation.wallpaper.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.domain.pic.PicInteractor
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.base.ProgressUi
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.filters.FiltersUi
import wallgram.hd.wallpapers.presentation.pic.PicUi
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val interactor: PicInteractor,
    dispatchers: Dispatchers
): BaseViewModel(dispatchers) {

    private val _pic = MutableLiveData<InfoListUi>()
    val pic: LiveData<InfoListUi> get() = _pic

    fun fetch(id: Int) {
        _pic.value = InfoListUi.Base(listOf(ProgressUi()))
        handle {
            interactor.info(id, atFinish) { _pic.value = it }
        }
    }

    private val atFinish = {

    }


}