package wallgram.hd.wallpapers.ui.favorite

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.databinding.ItemRecommendCategoryBinding
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.util.ColorFilterTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class TagsViewHolder(private val itemBinding: ItemRecommendCategoryBinding) :
        RecyclerView.ViewHolder(
                itemBinding.root
        ) {

    fun bind(item: Tag, onItemClicked: (Tag) -> Unit) {
        itemBinding.apply {

            nameText.text = item.name.capitalize()
            totalText.text = item.total.toString()

            item.background?.let {
                Glide.with(root.context).load(it)
                        .apply(RequestOptions.bitmapTransform(wallgram.hd.wallpapers.util.ColorFilterTransformation(Color.argb(80, 0, 0, 0))))
                        .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#252831"))))
                        .transition(DrawableTransitionOptions.withCrossFade(200))
                        .into(image)

            }

            root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }
}