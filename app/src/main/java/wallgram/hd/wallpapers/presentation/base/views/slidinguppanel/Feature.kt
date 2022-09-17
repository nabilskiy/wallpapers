package wallgram.hd.wallpapers.presentation.base.views.slidinguppanel

import wallgram.hd.wallpapers.presentation.base.BaseFragment

interface Feature {
    val fragment: BaseFragment<*, *>
    val anchorPoint: Float
}