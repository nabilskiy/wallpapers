package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.favorite.FavoritesEmptyUi
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import javax.inject.Inject

class HistoryMapper @Inject constructor(private val galleryMapper: GalleryDomain.Mapper<GalleryUi>) :
    GalleriesDomain.Mapper<GalleriesUi> {
    override fun map(
        source: List<GalleryDomain>,
        isEmpty: Boolean
    ): GalleriesUi {
        val result = mutableListOf<ItemUi>()
        if (source.isEmpty())
            result.add(
                FavoritesEmptyUi(
                    R.string.history_empty,
                    R.drawable.ic_history_large
                )
            )

        result.addAll(source.map { it.map(galleryMapper) })
        return GalleriesUi.Base(result)
    }
}