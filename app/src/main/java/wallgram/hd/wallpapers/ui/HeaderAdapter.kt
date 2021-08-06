package wallgram.hd.wallpapers.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.databinding.ItemListHeaderBinding

class HeaderAdapter(private val title: String) :
    RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemListHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            binding.titleText.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = 1
}