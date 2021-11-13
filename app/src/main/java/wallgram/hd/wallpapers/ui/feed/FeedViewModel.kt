package wallgram.hd.wallpapers.ui.feed

import wallgram.hd.wallpapers.data.repository.data.DataRepositorySource
import wallgram.hd.wallpapers.ui.base.BaseViewModel
import javax.inject.Inject

class FeedViewModel @Inject constructor(
        private val dataRepository: DataRepositorySource
): BaseViewModel(){


}