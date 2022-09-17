package wallgram.hd.wallpapers.domain.filters

import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.filters.NavigateFilter

interface CategoryDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(private val id: Int, private val name: String, private val background: String, private val sample: List<GalleryDomain>) :
        CategoryDomain {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(id, name, background, sample)
    }

    interface Mapper<T> {
        fun map(id: Int, name: String, background: String, sample: List<GalleryDomain>): T

        class Base(
            private val navigateFilter: NavigateFilter
        ) : Mapper<FilterUi.Base> {
            override fun map(id: Int, name: String, background: String, sample: List<GalleryDomain>) =
                FilterUi.Base(id, name, background, navigateFilter)
        }
    }
}