package wallgram.hd.wallpapers.presentation.base

import android.view.View
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemListHeaderBinding
import wallgram.hd.wallpapers.databinding.ViewToolbarBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class ToolbarViewHolder(
    private val binding: ViewToolbarBinding,
    private val clickListener: GenericAdapter.ClickListener<Unit>
) :
    GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) {
        item.show(binding.root)
        binding.root.handleClick{
            clickListener.click(Unit)
        }
    }
}