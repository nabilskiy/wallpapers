package wallgram.hd.wallpapers.ui.main

import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.databinding.ItemCategoryMenuBinding
import wallgram.hd.wallpapers.model.Category

class MenuCategoriesViewHolder(private val itemBinding: ItemCategoryMenuBinding) :
    RecyclerView.ViewHolder(
            itemBinding.root
    ) {

    fun bind(item: Category, onItemClicked: (Category) -> Unit) {
        itemBinding.apply {

            nameText.text = item.name.capitalize()

            root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }
}