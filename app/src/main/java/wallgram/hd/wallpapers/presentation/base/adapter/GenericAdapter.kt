package wallgram.hd.wallpapers.presentation.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.core.Mapper

abstract class GenericAdapter<T : ItemUi>(
    private val viewHolderFactoryChain: ViewHolderFactoryChain<T>
) : RecyclerView.Adapter<GenericViewHolder<T>>(), Mapper.Unit<List<T>>, SpanSizeLookupOwner {

    private val list: MutableList<T> = mutableListOf()

    override fun getItemViewType(position: Int): Int = list[position].type()

    override fun getSpanSizeLookup(spanCount: Int) = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) = try {
            list[position].getSpanSize(spanCount, position)
        } catch (e: IndexOutOfBoundsException) {
            spanCount
        }
    }

    fun getItems(): List<ItemUi> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> =
        viewHolderFactoryChain.viewHolder(parent, viewType)

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    override fun map(data: List<T>) {
        val diffCallback = DiffUtilCallback(list, data)
        val result = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    abstract class Base(viewHolderFactoryChain: ViewHolderFactoryChain<ItemUi>) :
        GenericAdapter<ItemUi>(viewHolderFactoryChain)


    interface ClickListener<T> {
        fun click(item: T)

        class Empty<T> : ClickListener<T> {
            override fun click(item: T) = Unit
        }
    }
}