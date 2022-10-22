package wallgram.hd.wallpapers.presentation.start

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.data.device.DeviceName
import wallgram.hd.wallpapers.data.resolution.ResolutionCacheDataSource
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.main.LaunchCacheDataSource
import wallgram.hd.wallpapers.util.modo.replace
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val launchCacheDataSource: LaunchCacheDataSource,
    private val resolutionCacheDataSource: ResolutionCacheDataSource,
    private val display: DisplayProvider,
    private val deviceName: DeviceName,
    private val communication: StartCommunication,
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {

    init {
        fetch()
    }

    private fun fetch() {
        val deviceName = deviceName.deviceName
        val resolution = display.getScreenSizeUi()

        val uiResult = StartUi.Base(deviceName, resolution)

        resolutionCacheDataSource.changeResolution(resolution.replace(" ", ""))

        communication.map(uiResult)
    }

    fun navigateHome() {
        launchCacheDataSource.changeFirstLaunch()
        modo.replace(Screens.MultiStack())
    }

    fun observe(owner: LifecycleOwner, observer: Observer<StartUi>) =
        communication.observe(owner, observer)

}