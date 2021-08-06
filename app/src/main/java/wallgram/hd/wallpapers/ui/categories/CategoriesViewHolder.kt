package wallgram.hd.wallpapers.ui.categories

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemCategoryBinding
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.util.ColorFilterTransformation
import wallgram.hd.wallpapers.util.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


class CategoriesViewHolder(private val itemBinding: ItemCategoryBinding, val tag: String) :
    RecyclerView.ViewHolder(
            itemBinding.root
    ) {

    val images = itemBinding.root.context.resources.obtainTypedArray(R.array.category_images)

    init {
        if(tag == CategoriesFragment::class.java.simpleName){
            itemView.layoutParams.height = 100.dp
            itemView.layoutParams.width = MATCH_PARENT
        }
        else {
            itemView.layoutParams.height = 90.dp
            itemView.layoutParams.width = 148.dp
        }
    }

    fun bind(item: Category, onItemClicked: (Category) -> Unit) {
        itemBinding.apply {

            name.text = item.name.capitalize()
            Glide.with(root.context).load(images.getDrawable(bindingAdapterPosition))

                    .apply(RequestOptions.bitmapTransform(wallgram.hd.wallpapers.util.ColorFilterTransformation(Color.argb(80, 0, 0, 0))))
                    .apply(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                    .apply(RequestOptions().placeholder(ColorDrawable(Color.parseColor("#252831"))))
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(image)

            root.setOnClickListener {
                onItemClicked.invoke(item)
            }
        }
    }
}