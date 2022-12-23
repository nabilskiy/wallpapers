package wallgram.hd.wallpapers.domain.gallery

import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.IsSubscribed
import wallgram.hd.wallpapers.data.ads.recyclerbanner.RecyclerBannerAd
import wallgram.hd.wallpapers.di.qualifiers.CarouselAdBanner
import wallgram.hd.wallpapers.presentation.ads.AdBannerUi
import wallgram.hd.wallpapers.presentation.base.*
import wallgram.hd.wallpapers.presentation.gallery.GalleriesUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.favorite.FavoritesEmptyUi
import wallgram.hd.wallpapers.presentation.search.SearchEmptyUi
import java.lang.NumberFormatException
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

        class Base @Inject constructor(
            private val galleryMapper: GalleryDomain.Mapper<GalleryUi>,
            private val adBanner: RecyclerBannerAd,
            private val subscription: IsSubscribed
        ) :
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

                if (!subscription.isSubscribed())
                    for (i in result.indices) {
                        if (i > 0 && (i - 21) % 22 == 0) {
                            result.add(i, AdBannerUi(adBanner))
                        }

                    }

                return GalleriesUi.Base(result)
            }
        }

        class Search @Inject constructor(
            private val galleryMapper: GalleryDomain.Mapper<GalleryUi>,
            private val adBanner: RecyclerBannerAd,
            private val subscription: IsSubscribed
        ) :
            Mapper<GalleriesUi> {

            override fun map(
                source: List<GalleryDomain>,
                isEmpty: Boolean
            ): GalleriesUi {
                val result = mutableListOf<ItemUi>()


                when {
                    source.isEmpty() -> result.add(SearchEmptyUi())
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

                if (!subscription.isSubscribed())
                    for (i in result.indices) {
                        if (i > 0 && (i - 21) % 22 == 0) {
                            result.add(i, AdBannerUi(adBanner))
                        }

                    }

                return GalleriesUi.Base(result)
            }
        }

        class Carousel @Inject constructor(
            private val galleryMapper: GalleryDomain.Mapper<GalleryUi>,
            @CarouselAdBanner private val adBanner: RecyclerBannerAd,
            private val subscription: IsSubscribed
        ) :
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

                if (!subscription.isSubscribed()) {
                    for (i in result.indices) {
                        if (i > 0 && (i - 10) % 11 == 0) {
                            result.add(i, AdBannerUi(adBanner))
                        }

                    }
                }


                return GalleriesUi.Base(result)
            }
        }

        class Position(private val id: Int) : Mapper<Int> {
            override fun map(source: List<GalleryDomain>, isEmpty: Boolean): Int =
                source.indexOfFirst { it.map(GalleryDomain.Mapper.CompareId(id)) }
        }

        class Link(private val id: Int) : Mapper<String> {
            override fun map(source: List<GalleryDomain>, isEmpty: Boolean): String =
                try {
                    source.first { it.map(GalleryDomain.Mapper.CompareId(id)) }
                        .map(GalleryDomain.Mapper.Link())
                } catch (e: NoSuchElementException) {
                    ""
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
                    result.add(FavoritesEmptyUi(R.string.favorites_empty, R.drawable.ic_favorite))

                result.addAll(source.map { it.map(galleryMapper) })
                return GalleriesUi.Base(result)
            }
        }

    }
}