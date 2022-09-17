package wallgram.hd.wallpapers.presentation.base.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemCarouselBinding
import wallgram.hd.wallpapers.presentation.base.adapter.*
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.filters.FilterUi

class CarouselViewHolderChain(
    private val adapter: GenericAdapter<ItemUi>,
    private val viewPool: RecyclerView.RecycledViewPool,
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 4){
            val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.recyclerView.setRecycledViewPool(viewPool)
            CarouselViewHolder(
                binding,
                adapter
            )
        }

        else viewHolderFactoryChain.viewHolder(parent, viewType)
}