package wallgram.hd.wallpapers.presentation.home

import android.graphics.Point
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType
import wallgram.hd.wallpapers.presentation.home.scroll.HomeCarouselViewHolderChain

class HomeAdapter(
    viewPool: RecyclerView.RecycledViewPool,
) : GenericAdapter.Base(
    ProgressViewHolderChain(
        HomeCarouselViewHolderChain(
            viewPool,
            ViewHolderFactoryChain.Exception()
        )
    )
)