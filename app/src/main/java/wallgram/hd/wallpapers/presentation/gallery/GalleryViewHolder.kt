package wallgram.hd.wallpapers.presentation.gallery

import android.graphics.Point
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.databinding.ItemSmallPhotoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.util.dp

class GalleryViewHolder(
    private val binding: ItemPhotoBinding
) : GenericViewHolder<ItemUi>(binding.root) {

    override fun bind(item: ItemUi) = with(binding) {
        item.show(ivPhoto)
    }
}

class GallerySmallViewHolder(
    private val binding: ItemSmallPhotoBinding
) : GenericViewHolder<ItemUi>(binding.root) {

    override fun bind(item: ItemUi) = with(binding) {
        ivPhoto.shapeAppearanceModel = ivPhoto.shapeAppearanceModel.toBuilder()
            .setAllCornerSizes(4f.dp).build()
        item.show(ivPhoto)
    }
}