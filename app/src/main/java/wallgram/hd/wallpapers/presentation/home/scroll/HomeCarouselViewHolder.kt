package wallgram.hd.wallpapers.presentation.home.scroll

import android.graphics.Point
import wallgram.hd.wallpapers.databinding.ItemHomeCarouselBinding
import wallgram.hd.wallpapers.presentation.base.CustomRecyclerView
import wallgram.hd.wallpapers.presentation.base.adapter.CarouselItemDecoration
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType
import wallgram.hd.wallpapers.util.dp

class HomeCarouselViewHolder(
    private val binding: ItemHomeCarouselBinding
) : GenericViewHolder<ItemUi>(binding.root) {

    private val carouselDecoration = CarouselItemDecoration(8.dp)

    override fun bind(item: ItemUi) = with(binding) {
        item.show(
            titleText
        )

        val adapter = CustomRecyclerView.HomeAdapter()
        item.showCarousel(adapter, recyclerView)

        recyclerView.removeItemDecoration(carouselDecoration)
        recyclerView.addItemDecoration(carouselDecoration)
    }
}