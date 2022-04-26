package wallgram.hd.wallpapers.ui.wallpapers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdViewHolder
import wallgram.hd.wallpapers.databinding.ItemNativeAdBinding
import wallgram.hd.wallpapers.databinding.ItemPhotoBinding
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.util.*

class WallpapersAdapter(private val onItemClicked: ((Int, Int) -> Unit)) :
    PagingDataAdapter<Any, BaseViewHolder<Any>>(DiffCallback()),
    SpanSizeLookupOwner,
    ItemDecorationOwner {

    companion object {
        const val POST = 1
        const val NATIVE_AD = 5

        const val NUM_OF_POST_BEFORE_ADS = 4


    }

    private val nativeAds = mutableListOf<NativeAd>()

    fun addNativeAd(ad: NativeAd) {
        nativeAds.add(ad)
    }


    inner class ViewHolder(private val binding: ItemPhotoBinding) :
        BaseViewHolder<Gallery>(binding.root) {

        init {
            itemView.layoutParams.height =
                wallgram.hd.wallpapers.util.DynamicParams.instance.a(itemView.context).y
        }

        fun bind(item: Gallery) {

        }

        override fun performBind(item: Gallery?) {
            var preview = item?.preview ?: ""
            if(preview.contains("akspic.ru"))
                preview = preview.replace("akspic.ru", "wallspic.com")

            with(binding) {
                Glide.with(root.context).load(preview)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#1B1928"))))
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .into(ivPhoto)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Gallery && newItem is Gallery) oldItem.id == newItem.id else false
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Gallery && newItem is Gallery) oldItem == newItem else false
        }
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val gallery = getItem(position)
//        gallery?.let {
//            holder.bind(it)
//        }
//
//    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
        when (getItemViewType(position)) {
            NATIVE_AD -> {
                val adPosition = position * nativeAds.size / itemCount
                val nativeAd = nativeAds[adPosition]
                holder.performBind(nativeAd)
            }
            else -> holder.performBind(getItem(position))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        return when (viewType) {
            NATIVE_AD -> NativeAdViewHolder(
                ItemNativeAdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ) as BaseViewHolder<Any>
            else -> {
                val binding =  ItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                val viewHolder = ViewHolder(
                    binding
                ) as BaseViewHolder<Any>
                binding.root.setOnClickListener {
                    val position = viewHolder.bindingAdapterPosition
                    if (position>-1){
                        val id = (getItem(position) as Gallery)?.id ?: -1
                        if (position != RecyclerView.NO_POSITION && id != -1)
                            onItemClicked(position, id)
                    }

                }
                return viewHolder

            }
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val viewHolder = ViewHolder(binding)
//        binding.root.setOnClickListener {
//            val position = viewHolder.bindingAdapterPosition
//            val id = getItem(position)?.id ?: -1
//            if (position != RecyclerView.NO_POSITION && id != -1)
//                onItemClicked(position, id)
//        }
//        return viewHolder
//    }

    override fun getItemViewType(position: Int): Int {
        val nativeAdRows = getNativeAdRows()
        return if (hasNativeAds() && nativeAdRows.contains(position)) {
            NATIVE_AD
        } else {
            POST
        }
    }


    fun onNativeAdLoaded() {
        val rows = getNativeAdRows()
        rows.forEach {
            notifyItemInserted(it)
        }
    }

    private fun hasNativeAds(): Boolean {
        return nativeAds.isNotEmpty()
    }

    private fun getNativeAdRows(): List<Int> {
        val rows = mutableListOf<Int>()
        for (i in 0..itemCount) {
            if (i != 0 && i % NUM_OF_POST_BEFORE_ADS == 0) {
                rows.add(i)
            }
        }
        return rows
    }

    override fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup = SimpleSpanSizeLookup(1)
    override fun getItemDecorations(): List<RecyclerView.ItemDecoration> =
        listOf(SimpleItemDecoration(2.dp))

}