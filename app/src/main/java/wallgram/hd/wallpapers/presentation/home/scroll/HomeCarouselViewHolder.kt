package wallgram.hd.wallpapers.presentation.home.scroll

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemHomeCarouselBinding
import wallgram.hd.wallpapers.presentation.base.CustomRecyclerView
import wallgram.hd.wallpapers.presentation.base.adapter.CarouselItemDecoration
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.util.dp

class HomeCarouselViewHolder(
    private val binding: ItemHomeCarouselBinding,
    private val clickListener: GenericAdapter.ClickListener<Pair<Int, Int>>
) : GenericViewHolder<ItemUi>(binding.root) {

    private val carouselDecoration = CarouselItemDecoration(8.dp)

    override fun bind(item: ItemUi) = with(binding) {
        item.show(
            titleText
        )

        val adapter = CustomRecyclerView.HomeAdapter(clickListener)
        item.showCarousel(adapter, recyclerView)

        recyclerView.removeItemDecoration(carouselDecoration)
        recyclerView.addItemDecoration(carouselDecoration)
    }
}