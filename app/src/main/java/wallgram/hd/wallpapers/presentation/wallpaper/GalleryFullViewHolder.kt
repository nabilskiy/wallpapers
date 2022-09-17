package wallgram.hd.wallpapers.presentation.wallpaper

import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemFullPhotoBinding
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi

class GalleryFullViewHolder(
    private val binding: ItemFullPhotoBinding,
    private val clickListener: GenericAdapter.ClickListener<GalleryUi>
) : GenericViewHolder<ItemUi>(binding.root) {
    override fun bind(item: ItemUi) = with(binding){
        item.show(ivPhoto)
        ivPhoto.setOnClickListener {
            clickListener.click(item as GalleryUi)
        }
    }
}