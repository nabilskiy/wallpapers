package wallgram.hd.wallpapers.presentation.wallpapers

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolder
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ToolbarViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.*

class SimilarAdapter(
    clickListener: ClickListener<Pair<Int, Int>>,
    toolbarClickListener: ClickListener<Unit>
) : GenericAdapter.Base(
    ProgressViewHolderChain(
        ToolbarViewHolderChain(
            toolbarClickListener,
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
)