package wallgram.hd.wallpapers.presentation.main

import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.util.modo.Screen

interface FirstLaunchUi {

    fun screen(): Screen

    class Base(private val firstLaunch: Boolean) : FirstLaunchUi {
        override fun screen() = if (firstLaunch) Screens.Start() else Screens.MultiStack()
    }

}