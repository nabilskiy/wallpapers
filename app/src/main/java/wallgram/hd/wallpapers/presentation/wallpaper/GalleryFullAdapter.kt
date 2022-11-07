package wallgram.hd.wallpapers.presentation.wallpaper

import wallgram.hd.wallpapers.presentation.ads.AdBannerUi
import wallgram.hd.wallpapers.presentation.ads.AdBannerViewHolderChain
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain

class GalleryFullAdapter : GenericAdapter.Base(
    ProgressViewHolderChain(
        BottomFullProgressViewHolderChain(
            AdBannerViewHolderChain(
                GalleryFullViewHolderChain(
                    ViewHolderFactoryChain.Exception()
                )
            )
        )
    )
)