package wallgram.hd.wallpapers.presentation.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.data.resolution.ResolutionCacheDataSource
import wallgram.hd.wallpapers.domain.resolution.ResolutionsInteractor
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.settings.SettingsUi
import wallgram.hd.wallpapers.presentation.subscribe.UpdateSubscriptions
import wallgram.hd.wallpapers.presentation.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    launchCacheDataSource: LaunchCacheDataSource,
    private val resolutionsInteractor: ResolutionsInteractor,
    private val displayProvider: DisplayProvider,
    private val communication: MainCommunication,
    private val update: UpdateSubscriptions.Observe,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    init {
        val isFirstLaunch = launchCacheDataSource.isFirstLaunch()
        communication.map(FirstLaunchUi.Base(isFirstLaunch))

        fetchResolution()
    }

    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Boolean>) =
        update.observe(owner, observer)

    private fun fetchResolution() {
        val savedResolution = resolutionsInteractor.currentResolution()
        if (savedResolution.isEmpty()) {
            val display = displayProvider.getScreenSizeUi()
            //  resolutionsInteractor.changeResolution(display.replace(" ", ""))
        }

    }

    fun observe(owner: LifecycleOwner, observer: Observer<FirstLaunchUi>) =
        communication.observe(owner, observer)

}