package wallgram.hd.wallpapers.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import wallgram.hd.wallpapers.databinding.ItemCategoryBinding
import wallgram.hd.wallpapers.model.Category

class CategoriesListAdapter(private val onItemClicked: ((Category) -> Unit), private val tag: String) : ListAdapter<Category, CategoriesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(itemBinding, tag)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = getItem(position)

            holder.apply {
                bind(category, onItemClicked)
            }

    }

    private class DiffCallback : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.equals(newItem)
        }
    }

}


