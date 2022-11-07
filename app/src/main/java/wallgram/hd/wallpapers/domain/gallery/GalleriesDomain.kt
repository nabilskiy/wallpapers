package wallgram.hd.wallpapers.domain.gallery

import coil.memory.MemoryCache
import dagger.hilt.InstallIn
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.presentation.ads.AdBannerUi
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
        fun map(
            source: List<GalleryDomain>,
            isEmpty: Boolean
        ): T

        class Base @Inject constructor(private val galleryMapper: GalleryDomain.Mapper<GalleryUi>) :
            Mapper<GalleriesUi> {

            override fun map(
                source: List<GalleryDomain>,
                isEmpty: Boolean
            ): GalleriesUi {
                val result = mutableListOf<ItemUi>()

                val isAds = false

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

                if (isAds) {
                    result.add(9, AdBannerUi())
                }


                return GalleriesUi.Base(result)
            }
        }

        class Favorites @Inject constructor(
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

    }
}