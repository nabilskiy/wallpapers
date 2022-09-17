package wallgram.hd.wallpapers.presentation.categories

import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.CustomRecyclerView
import wallgram.hd.wallpapers.presentation.filters.FilterViewHolderChain
import wallgram.hd.wallpapers.presentation.base.ProgressViewHolderChain
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.base.carousel.CarouselViewHolderChain
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.filters.FilterUi

class CategoryAdapter(
    colorAdapter: CustomRecyclerView.ColorsAdapter,
    viewPool: RecyclerView.RecycledViewPool
) : GenericAdapter.Base(
    HeaderViewHolderChain(
        ProgressViewHolderChain(
            FilterViewHolderChain(
                CarouselViewHolderChain(
                    colorAdapter,
                    viewPool,
                    ViewHolderFactoryChain.Exception()
                )
            )
        )
    )
)