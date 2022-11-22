package wallgram.hd.wallpapers.presentation.base.carousel

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemCarouselBinding
import wallgram.hd.wallpapers.presentation.base.adapter.CarouselItemDecoration
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.util.dp

class CarouselViewHolder(
    private val binding: ItemCarouselBinding,
    private val carouselAdapter: GenericAdapter<*>
) : GenericViewHolder<ItemUi>(binding.root) {

    private val carouselDecoration = CarouselItemDecoration(8.dp)

    override fun bind(item: ItemUi) = with(binding) {

        item.showCarousel(carouselAdapter, recyclerView)

        recyclerView.removeItemDecoration(carouselDecoration)
        recyclerView.addItemDecoration(carouselDecoration)
    }
}