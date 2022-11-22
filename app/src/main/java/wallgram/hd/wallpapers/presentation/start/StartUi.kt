package wallgram.hd.wallpapers.presentation.start

import wallgram.hd.wallpapers.presentation.base.adapter.MyView

interface StartUi {

    fun show(nameContainer: MyView, resolutionContainer: MyView)

    class Base(private val deviceName: String, private val screenResolution: String) : StartUi {
        override fun show(nameContainer: MyView, resolutionContainer: MyView) {
            nameContainer.show(deviceName)
            resolutionContainer.show(screenResolution)
        }
    }
}
