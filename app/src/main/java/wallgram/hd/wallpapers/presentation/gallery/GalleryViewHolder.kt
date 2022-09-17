package wallgram.hd.wallpapers.presentation.gallery

import android.view.View
import com.google.android.material.shape.CornerSize
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi

class GalleryViewHolder(
    private val binding: ItemPhotoBinding,
    private val galleryViewType: GalleryViewType,
    private val clickListener: GenericAdapter.ClickListener<Pair<Int, Int>>
) : GenericViewHolder<ItemUi>(binding.root) {

    init {
        val size = galleryViewType.size()

        itemView.layoutParams.apply {
            width = size.x
            height = size.y
        }
    }

    override fun bind(item: ItemUi) = with(binding) {
        ivPhoto.shapeAppearanceModel = ivPhoto.shapeAppearanceModel.toBuilder()
            .setAllCornerSizes(galleryViewType.corners()).build()
        item.show(ivPhoto)
        ivPhoto.setOnClickListener {
            val id = item.filter()
            clickListener.click(Pair(bindingAdapterPosition, id))
        }
    }
}