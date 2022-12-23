package wallgram.hd.wallpapers.presentation.changer

import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ChangerViewModel @Inject constructor(
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {
}