package wallgram.hd.wallpapers.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import wallgram.hd.wallpapers.databinding.ItemCategoryMenuBinding
import wallgram.hd.wallpapers.model.Category

class MenuCategoriesListAdapter(private val onItemClicked: ((Category) -> Unit)) : ListAdapter<Category, MenuCategoriesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCategoriesViewHolder {
        val itemBinding =
            ItemCategoryMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuCategoriesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MenuCategoriesViewHolder, position: Int) {
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


