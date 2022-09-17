package wallgram.hd.wallpapers.presentation.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.*

class GalleryViewHolderChain(
    private val galleryViewType: GalleryViewType,
    private val clickListener: GenericAdapter.ClickListener<Pair<Int, Int>>,
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 6)
            GalleryViewHolder(
                ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false), galleryViewType, clickListener
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}