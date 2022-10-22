package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.presentation.base.adapter.*
import wallgram.hd.wallpapers.presentation.colors.ColorViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GallerySmallViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewHolderChain
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType

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

    class HomeAdapter() :
        GenericAdapter.Base(
            GallerySmallViewHolderChain(
                ViewHolderFactoryChain.Exception()
            )
        )

}

