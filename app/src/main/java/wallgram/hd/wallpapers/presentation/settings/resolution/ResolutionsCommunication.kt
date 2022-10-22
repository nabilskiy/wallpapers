package wallgram.hd.wallpapers.presentation.settings.resolution

import wallgram.hd.wallpapers.core.presentation.Communication
import javax.inject.Inject


interface ResolutionsCommunication : Communication.Mutable<ResolutionsUi> {
    class Base @Inject constructor() : Communication.UiUpdate<ResolutionsUi>(),
        ResolutionsCommunication
}