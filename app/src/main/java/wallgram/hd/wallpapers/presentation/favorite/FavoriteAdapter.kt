package wallgram.hd.wallpapers.presentation.favorite

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.categories.HeaderViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType

class FavoriteAdapter : GenericAdapter.Base(
    HeaderViewHolderChain(
        FavoritesEmptyViewHolderChain(
            ProgressViewHolderChain(
                BottomProgressViewHolderChain(
                    GalleryViewHolderChain(
                        ViewHolderFactoryChain.Exception()
                    )
                )
            )
        )
    )
)

class HistoryAdapter : GenericAdapter.Base(
    HeaderViewHolderChain(
        FavoritesEmptyViewHolderChain(
            ProgressViewHolderChain(
                BottomProgressViewHolderChain(
                    GalleryViewHolderChain(
                        ViewHolderFactoryChain.Exception()
                    )
                )
            )
        )
    )
)