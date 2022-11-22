package wallgram.hd.wallpapers.presentation.settings.language

import dagger.hilt.android.lifecycle.HiltViewModel
import wallgram.hd.wallpapers.core.Dispatchers
import wallgram.hd.wallpapers.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    dispatchers: Dispatchers
) : BaseViewModel(dispatchers) {
}