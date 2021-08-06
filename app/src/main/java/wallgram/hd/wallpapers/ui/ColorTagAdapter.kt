package wallgram.hd.wallpapers.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import wallgram.hd.wallpapers.databinding.ItemColorTagBinding

class ColorTagAdapter(private val onItemClicked: ((Int) -> Unit)) : ListAdapter<Int, ColorTagViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorTagViewHolder {
        val itemBinding =
            ItemColorTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorTagViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ColorTagViewHolder, position: Int) {
        val color = getItem(position)

            holder.apply {
                bind(color, onItemClicked)
            }

    }

    private class DiffCallback : DiffUtil.ItemCallback<Int>() {

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

}


