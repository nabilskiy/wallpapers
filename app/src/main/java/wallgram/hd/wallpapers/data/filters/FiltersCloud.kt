package wallgram.hd.wallpapers.data.filters

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.GalleryCloud
import wallgram.hd.wallpapers.data.gallery.GalleryData
import wallgram.hd.wallpapers.domain.filters.CategoryDomain
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.domain.home.HomeDomain

interface FiltersCloud {

    fun <T> map(mapper: Mapper<T>): T

    class Empty : FiltersCloud {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(-1, "", "", listOf())
    }

    @Keep
    data class Base(
        @SerializedName("id")
        private val id: Int,
        @SerializedName("title")
        private val title: String,
        @SerializedName("background_src")
        private val background: String,
        @SerializedName("sample")
        private val sample: List<GalleryCloud.Base>
    ) : FiltersCloud {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(id, title, background, sample)
    }

    interface Mapper<T> {
        fun map(id: Int, title: String, background: String, sample: List<GalleryCloud>): T

        class Base(private val galleryMapper: GalleryData.Mapper<GalleryDomain>) :
            Mapper<CategoryDomain> {
            override fun map(
                id: Int,
                title: String,
                background: String,
                sample: List<GalleryCloud>
            ): CategoryDomain {
                val sampleData = sample.map { it.map(GalleryCloud.Mapper.Base()) }
                val list = sampleData.map { it.map(galleryMapper) }
                return CategoryDomain.Base(id, title, background, list)
            }
        }

        class Home(private val galleryMapper: GalleryData.Mapper<GalleryDomain>) :
            Mapper<HomeDomain> {
            override fun map(
                id: Int,
                title: String,
                background: String,
                sample: List<GalleryCloud>
            ): HomeDomain {
                val sampleData = sample.map { it.map(GalleryCloud.Mapper.Base()) }
                val list = sampleData.map { it.map(galleryMapper, id, "filter_$id") }
                return HomeDomain.Base(id, title, background, list)
            }
        }

        class Test :
            Mapper<WallpaperRequest> {
            override fun map(
                id: Int,
                title: String,
                background: String,
                sample: List<GalleryCloud>
            ): WallpaperRequest {
                return WallpaperRequest.Filter(id, title).apply {
                    setData(sample.map { it.map(GalleryCloud.Mapper.Base()) })
                    nextPage()
                }
            }
        }


        class Id(private val id: Int) :
            Mapper<Boolean> {
            override fun map(
                id: Int,
                title: String,
                background: String,
                sample: List<GalleryCloud>
            ): Boolean {
                return this.id == id
            }
        }
    }
}