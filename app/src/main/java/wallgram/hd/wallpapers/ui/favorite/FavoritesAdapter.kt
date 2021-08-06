package wallgram.hd.wallpapers.ui.favorite

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import wallgram.hd.wallpapers.databinding.ItemFavoritePhotoBinding
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.util.DynamicParams

class FavoritesAdapter(
    private val onItemClicked: ((Int, Int) -> Unit),
    private val onItemDelete: ((Gallery) -> Unit)
) : ListAdapter<Gallery, FavoritesAdapter.ViewHolder>(DiffCallback()) {


    inner class ViewHolder(private val binding: ItemFavoritePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.layoutParams.height =
                wallgram.hd.wallpapers.util.DynamicParams.instance.a(itemView.context).y
        }

        fun bind(item: Gallery) {
            with(binding) {
                root.setOnClickListener {
                    onItemClicked(bindingAdapterPosition, item.id)
                }

                deleteBtn.setOnClickListener {
                    onItemDelete(item)
                }

                Glide.with(root.context).load(item.preview)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                    .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#1B1928"))))
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .into(ivPhoto)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Gallery>() {

        override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gallery = getItem(position)
        gallery?.let {
            holder.bind(it)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemFavoritePhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

}