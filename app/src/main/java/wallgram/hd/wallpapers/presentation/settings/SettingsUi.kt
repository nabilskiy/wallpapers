package wallgram.hd.wallpapers.presentation.settings

import wallgram.hd.wallpapers.presentation.base.adapter.MyView

interface SettingsUi {

    fun show(view: MyView)

    class Base(private val cacheSize: String) : SettingsUi {
        override fun show(view: MyView) =
            view.show(cacheSize)
    }

}
