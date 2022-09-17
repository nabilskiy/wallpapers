package wallgram.hd.wallpapers.presentation.favorite

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolder
import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.categories.HeaderViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType
import wallgram.hd.wallpapers.presentation.search.SearchEmptyUi
import wallgram.hd.wallpapers.presentation.search.SearchEmptyViewHolderChain

class FavoriteAdapter(clickListener: ClickListener<Pair<Int, Int>>) : GenericAdapter.Base(
    HeaderViewHolderChain(
        FavoritesEmptyViewHolderChain(
            ProgressViewHolderChain(
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