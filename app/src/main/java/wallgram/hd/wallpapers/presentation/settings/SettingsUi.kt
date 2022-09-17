package wallgram.hd.wallpapers.presentation.settings

import wallgram.hd.wallpapers.presentation.base.TextContainer

interface SettingsUi{

    fun show(container: TextContainer)

    class Base(private val cacheSize: String): SettingsUi{
        override fun show(container: TextContainer) =
            container.show(cacheSize)
    }

}
