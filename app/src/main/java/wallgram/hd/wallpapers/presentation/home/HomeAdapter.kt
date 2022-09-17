package wallgram.hd.wallpapers.presentation.home

import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.CustomRecyclerView
import wallgram.hd.wallpapers.presentation.filters.FilterViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.base.carousel.CarouselViewHolderChain
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.colors.ColorViewHolderChain
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain
import wallgram.hd.wallpapers.presentation.home.scroll.HomeCarouselUi
import wallgram.hd.wallpapers.presentation.home.scroll.HomeCarouselViewHolderChain

class HomeAdapter(clickListener: ClickListener<Pair<Int, Int>>, viewPool: RecyclerView.RecycledViewPool): GenericAdapter.Base(
    ProgressViewHolderChain(
        HomeCarouselViewHolderChain(
            clickListener,
            viewPool,
            ViewHolderFactoryChain.Exception()
        )
    )
)