package wallgram.hd.wallpapers.presentation.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

interface ViewHolderFactoryChain<T : ItemUi> {

    fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T>

    class Exception<T : ItemUi> : ViewHolderFactoryChain<T> {
        override fun viewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
            throw IllegalStateException("unknown viewType $viewType")
        }
    }
}