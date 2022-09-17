package wallgram.hd.wallpapers.presentation.colors

import wallgram.hd.wallpapers.core.presentation.Communication
import wallgram.hd.wallpapers.data.colors.Color
import javax.inject.Inject

interface ColorsCommunication : Communication.Mutable<List<Color>> {
    class Base @Inject constructor() : Communication.UiUpdate<List<Color>>(), ColorsCommunication
}
