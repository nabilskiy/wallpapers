package wallgram.hd.wallpapers.presentation.wallpapers

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.*

class SimilarAdapter: GenericAdapter.Base(
    ProgressViewHolderChain(
        FullSizeErrorViewHolderChain(
            BottomErrorViewHolderChain(
                BottomProgressViewHolderChain(
                    GalleryViewHolderChain(
                        ViewHolderFactoryChain.Exception()
                    )
                )
            )
        )
    )
)