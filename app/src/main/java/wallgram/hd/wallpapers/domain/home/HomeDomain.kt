package wallgram.hd.wallpapers.domain.home

import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.presentation.filters.NavigateCarousel
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.home.scroll.HomeCarouselUi

interface HomeDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val id: Int,
        private val name: String,
        private val background: String,
        private val sample: List<GalleryDomain>
    ) :
        HomeDomain {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(id, name, background, sample)
    }

    interface Mapper<T> {
        fun map(id: Int, name: String, background: String, sample: List<GalleryDomain>): T

        class Base(
            private val mapper: GalleryDomain.Mapper<GalleryUi>,
            private val navigateCarousel: NavigateCarousel
        ) :
            Mapper<HomeCarouselUi.Base> {
            override fun map(
                id: Int,
                name: String,
                background: String,
                sample: List<GalleryDomain>
            ): HomeCarouselUi.Base {
                val list = sample.map { it.map(mapper) }
                return HomeCarouselUi.Base(id, name, list, navigateCarousel)
            }
        }


    }
}