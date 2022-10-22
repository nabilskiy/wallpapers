package wallgram.hd.wallpapers.presentation.gallery

import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.databinding.ItemSmallPhotoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.*

class GalleryViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 6)
            GalleryViewHolder(
                ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}

class GallerySmallViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 6)
            GallerySmallViewHolder(
                ItemSmallPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}