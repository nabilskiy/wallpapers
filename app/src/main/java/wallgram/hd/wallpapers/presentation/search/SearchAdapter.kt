package wallgram.hd.wallpapers.presentation.search

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolder
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.BottomErrorViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType

class SearchAdapter(clickListener: ClickListener<Pair<Int, Int>>) : GenericAdapter.Base(
    ProgressViewHolderChain(
        SearchViewHolderChain(
            SearchEmptyViewHolderChain(
                BottomErrorViewHolderChain(
                BottomProgressViewHolderChain(
                    GalleryViewHolderChain(
                        GalleryViewType.Default(),
                        clickListener,
                        ViewHolderFactoryChain.Exception()
                    ))
                )
            )
        )
    )
)