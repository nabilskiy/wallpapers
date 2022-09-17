package wallgram.hd.wallpapers.presentation.wallpaper

import wallgram.hd.wallpapers.presentation.base.BottomProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain

class GalleryFullAdapter(clickListener: ClickListener<GalleryUi>) : GenericAdapter.Base(
    ProgressViewHolderChain(
        BottomFullProgressViewHolderChain(
            GalleryFullViewHolderChain(
                clickListener,
                ViewHolderFactoryChain.Exception()
            )
        )
    )
)