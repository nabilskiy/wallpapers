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
import java.util.*


class CategoriesViewHolder(private val itemBinding: ItemCategoryBinding, val tag: String) :
    RecyclerView.ViewHolder(
        itemBinding.root
    ) {

    val images = arrayOf(
        R.drawable.image_01,
        R.drawable.image_02,
        R.drawable.image_03,
        R.drawable.image_04,
        R.drawable.image_05,
        R.drawable.image_06,
        R.drawable.image_07,
        R.drawable.image_08,
        R.drawable.image_09,
        R.drawable.image_10,
        R.drawable.image_11,
        R.drawable.image_12,
        R.drawable.image_13,
        R.drawable.image_14,
        R.drawable.image_15,
        R.drawable.image_16,
        R.drawable.image_17,
        R.drawable.image_18,
        R.drawable.image_19,
        R.drawable.image_20,
        R.drawable.image_21,
        R.drawable.image_22
    )


    init {
        if (tag == CategoriesFragment::class.java.simpleName) {
            itemView.layoutParams.height = 100.dp
            itemView.layoutParams.width = MATCH_PARENT
        } else {
            itemView.layoutParams.height = 90.dp
            itemView.layoutParams.width = 148.dp
        }
    }

    fun bind(item: Category, onItemClicked: (Category) -> Unit) {
        itemBinding.apply {

            name.text = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            Glide.with(root.context).load(images[bindingAdapterPosition])

                .apply(
                    RequestOptions.bitmapTransform(
                        wallgram.hd.wallpapers.util.ColorFilterTransformation(
                            Color.argb(80, 0, 0, 0)
                        )
                    )
                )
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