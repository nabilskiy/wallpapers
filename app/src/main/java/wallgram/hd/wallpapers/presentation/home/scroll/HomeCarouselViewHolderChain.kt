package wallgram.hd.wallpapers.presentation.home.scroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemHomeCarouselBinding
import wallgram.hd.wallpapers.presentation.base.adapter.*
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi

class HomeCarouselViewHolderChain(
    private val clickListener: GenericAdapter.ClickListener<Pair<Int, Int>>,
    private val viewPool: RecyclerView.RecycledViewPool,
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 7) {
            val binding =
                ItemHomeCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.recyclerView.setRecycledViewPool(viewPool)
            HomeCarouselViewHolder(
                binding,
                clickListener
            )
        } else viewHolderFactoryChain.viewHolder(parent, viewType)
}