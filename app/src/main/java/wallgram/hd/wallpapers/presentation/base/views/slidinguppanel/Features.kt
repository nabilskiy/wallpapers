package wallgram.hd.wallpapers.presentation.base.views.slidinguppanel

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.dialogs.DownloadDialogFragment
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.presentation.wallpaper.info.InfoDialogFragment
import wallgram.hd.wallpapers.presentation.wallpapers.SimilarFragment
import wallgram.hd.wallpapers.presentation.wallpapers.WallpapersFragment

object Features {

    class Similar(private val wallpaperRequest: WallpaperRequest) : Feature {
        override val fragment: BaseFragment<*, *> = SimilarFragment.newInstance(wallpaperRequest)
        override val anchorPoint: Float = 1f
    }

    class Wallpaper(private val wallpaperRequest: WallpaperRequest) : Feature {
        override val fragment: BaseFragment<*, *> = WallpapersFragment.newInstance(wallpaperRequest)
        override val anchorPoint: Float = 1f
    }

    class Info(private val id: Int) : Feature {
        override val fragment: BaseFragment<*, *> = InfoDialogFragment.create(id)
        override val anchorPoint: Float = 1f
    }

    class Download(resolution: String) : Feature {
        override val fragment: BaseFragment<*, *> = DownloadDialogFragment.newInstance(resolution)
        override val anchorPoint: Float = 1f
    }

}