package wallgram.hd.wallpapers.ui.favorite.tags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import wallgram.hd.wallpapers.databinding.ItemRecommendCategoryBinding
import wallgram.hd.wallpapers.model.Tag

class TagsAdapter(private val onItemClicked: ((Tag) -> Unit)) : ListAdapter<Tag, TagsViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val itemBinding =
                ItemRecommendCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = if (currentList.size > 6) 6 else currentList.size

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        val tag = getItem(position)

        holder.apply {
            bind(tag, onItemClicked)
        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<Tag>() {

        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.total == newItem.total
        }
    }

}


