package wallgram.hd.wallpapers.presentation.settings.resolution

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.data.resolution.ResolutionCacheDataSource
import wallgram.hd.wallpapers.domain.gallery.GalleryRepository
import wallgram.hd.wallpapers.domain.resolution.ResolutionsInteractor
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ResolutionViewModel @Inject constructor(
    private val interactor: ResolutionsInteractor,
    private val repository: GalleryRepository,
    private val communication: ResolutionsCommunication,
    private val update: UpdateResolutions.Observe,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    init {
        update()
    }

    fun update() = communication.map(interactor.resolutions())

    fun observe(owner: LifecycleOwner, observer: Observer<ResolutionsUi>) =
        communication.observe(owner, observer)

    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Boolean>) =
        update.observe(owner, observer)

    fun clearAll() {
        repository.clearAll()
    }

}