package wallgram.hd.wallpapers.ui.wallpaper

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import wallgram.hd.wallpapers.databinding.ItemPagerImageBinding
import wallgram.hd.wallpapers.model.Gallery
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersAdapter

class WallpaperItemAdapter(private val onItemClicked: ((MotionEvent) -> Unit)) :
    PagingDataAdapter<Gallery, WallpaperItemAdapter.ViewHolder>(DiffCallback()) {


    inner class ViewHolder(private val binding: ItemPagerImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Gallery) {
            with(binding) {
                Glide.with(root.context).load(item.original)
                    .thumbnail(Glide.with(root.context).load(item.preview).centerCrop())
                    .apply(RequestOptions().skipMemoryCache(true))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.isVisible = false
                            return false
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .into(img)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPagerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        binding.img.setOnTouchListener { view, motionEvent ->
            val position = viewHolder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClicked(motionEvent)
            }
         true
        }
        return viewHolder
    }


}