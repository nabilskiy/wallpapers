package wallgram.hd.wallpapers.domain.gallery

import dagger.hilt.InstallIn
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.presentation.base.*
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.categories.HeaderUi
import wallgram.hd.wallpapers.presentation.favorite.FavoritesEmptyUi
import wallgram.hd.wallpapers.presentation.search.SearchEmptyUi
import javax.inject.Inject


interface GalleriesDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val gallery: List<GalleryDomain>,
        private val isEmpty: Boolean
    ) : GalleriesDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(gallery, isEmpty)
    }

    interface Mapper<T> {
        fun map(source: List<GalleryDomain>, isEmpty: Boolean): T

        class Base @Inject constructor(private val galleryMapper: GalleryDomain.Mapper<GalleryUi>) :
            Mapper<GalleriesUi> {

            override fun map(
                source: List<GalleryDomain>,
                isEmpty: Boolean
            ): GalleriesUi {
                val result = mutableListOf<ItemUi>()

                when {
                    source.isEmpty() -> result.add(ProgressUi())
                    source.size == 1 && source[0] is GalleryDomain.Error ->
                        result.add(FullSizeErrorUi())

                    source.last() is GalleryDomain.Base -> {
                        result.addAll(source.map { it.map(galleryMapper) })

                        if (!isEmpty)
                            result.add(BottomProgressUi())
                    }
                    source.last() is GalleryDomain.Error -> {
                        for (item in source)
                            if (item is GalleryDomain.Base)
                                result.add(item.map(galleryMapper))
                        result.add(BottomErrorUi())
                    }
                }

                return GalleriesUi.Base(result)
            }
        }

        class Favorites @Inject constructor(
            private val galleryMapper: GalleryDomain.Mapper<GalleryUi>,
            private val resourceProvider: ResourceProvider
        ) :
            Mapper<GalleriesUi> {

            override fun map(
                source: List<GalleryDomain>,
                isEmpty: Boolean
            ): GalleriesUi {
                val result = mutableListOf<ItemUi>()

                result.add(HeaderUi(resourceProvider.string(R.string.menu_favorites)))

                if (source.isEmpty())
                    result.add(FavoritesEmptyUi())
                result.addAll(source.map { it.map(galleryMapper) })
                return GalleriesUi.Base(result)
            }
        }

        class History @Inject constructor(
            private val galleryMapper: GalleryDomain.Mapper<GalleryUi>
        ) :
            Mapper<GalleriesUi> {

            override fun map(
                source: List<GalleryDomain>,
                isEmpty: Boolean
            ): GalleriesUi {
                val result = mutableListOf<ItemUi>()
                if (source.isEmpty())
                    result.add(FavoritesEmptyUi())
                result.addAll(source.map { it.map(galleryMapper) })
                return GalleriesUi.Base(result)
            }
        }

        class Similar @Inject constructor(
            private val galleryMapper: GalleryDomain.Mapper<GalleryUi>,
            private val resourceProvider: ResourceProvider
        ) :
            Mapper<GalleriesUi> {

            override fun map(
                source: List<GalleryDomain>,
                isEmpty: Boolean
            ): GalleriesUi {
                val result = mutableListOf<ItemUi>()

                result.add(ToolbarUi(resourceProvider.string(R.string.similar_wallpapers)))

                when {
                    source.isEmpty() -> result.add(ProgressUi())
                    source.size == 1 && source[0] is GalleryDomain.Error ->
                        result.add(FullSizeErrorUi())

                    source.last() is GalleryDomain.Base -> {
                        result.addAll(source.map { it.map(galleryMapper) })

                        if (!isEmpty)
                            result.add(BottomProgressUi())
                    }
                    source.last() is GalleryDomain.Error -> {
                        for (item in source)
                            if (item is GalleryDomain.Base)
                                result.add(item.map(galleryMapper))
                        result.add(BottomErrorUi())
                    }
                }

                return GalleriesUi.Base(result)
            }
        }
    }
}