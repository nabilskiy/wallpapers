package wallgram.hd.wallpapers.presentation.feed

import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {


}