package wallgram.hd.wallpapers.presentation.wallpaper

import android.view.LayoutInflater
import android.view.ViewGroup
import wallgram.hd.wallpapers.databinding.ItemFullPhotoBinding
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.GenericViewHolder
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.ViewHolderFactoryChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolder

class GalleryFullViewHolderChain(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>
) : ViewHolderFactoryChain<ItemUi> {

    override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ItemUi> =
        if (viewType == 6)
            GalleryFullViewHolder(
                ItemFullPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        else viewHolderFactoryChain.viewHolder(parent, viewType)
}