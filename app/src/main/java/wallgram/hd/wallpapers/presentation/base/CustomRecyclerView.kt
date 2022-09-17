package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.adapter.*
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.colors.ColorViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType
import wallgram.hd.wallpapers.util.dp

class CustomRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs), MyView {

    override fun <T : ItemUi> show(data: List<T>, carouselAdapter: GenericAdapter<*>) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        setHasFixedSize(true)
        adapter = carouselAdapter
        carouselAdapter.map(data as List<Nothing>)
    }

    class ColorsAdapter : GenericAdapter.Base(
        ColorViewHolderChain(
            ViewHolderFactoryChain.Exception()
        )
    )

    class HomeAdapter(clickListener: ClickListener<Pair<Int, Int>>) :
        GenericAdapter.Base(
            GalleryViewHolderChain(
                GalleryViewType.Small(),
                clickListener,
                ViewHolderFactoryChain.Exception()
            )
        )

}

