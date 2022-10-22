package wallgram.hd.wallpapers.presentation.wallpapers

import wallgram.hd.wallpapers.presentation.ads.AdBannerViewHolder
import wallgram.hd.wallpapers.presentation.ads.AdBannerViewHolderChain
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.*

class GalleryAdapter: GenericAdapter.Base(
    ProgressViewHolderChain(
        FullSizeErrorViewHolderChain(
            BottomErrorViewHolderChain(
                AdBannerViewHolderChain(
                    BottomProgressViewHolderChain(
                        GalleryViewHolderChain(
                            ViewHolderFactoryChain.Exception()
                        )
                    )
                )
            )
        )
    )
)