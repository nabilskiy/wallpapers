package wallgram.hd.wallpapers.presentation.wallpapers

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolder
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.*

class GalleryAdapter(clickListener: ClickListener<Pair<Int, Int>>) : GenericAdapter.Base(
    ProgressViewHolderChain(
        FullSizeErrorViewHolderChain(
            BottomErrorViewHolderChain(
                BottomProgressViewHolderChain(
                    GalleryViewHolderChain(
                        GalleryViewType.Default(),
                        clickListener,
                        ViewHolderFactoryChain.Exception()
                    )
                )
            )
        )
    )
)