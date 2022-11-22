package wallgram.hd.wallpapers.data.gallery

import androidx.annotation.Keep
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.model.Links

interface GalleryCloud {

    fun <T> map(mapper: Mapper<T>): T

    class Empty : GalleryCloud {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(-1, 1080, 1920, "", "", Links("", "", ""))
    }
    
    @Keep
    data class Base(
        @SerializedName("id")
        val id: Int,
        @SerializedName("width")
        val width: Int,
        @SerializedName("height")
        val height: Int,
        @SerializedName("preview")
        val preview: String,
        @SerializedName("original")
        val original: String? = null,
        @SerializedName("promoted")
        val promoted: Int,
        @SerializedName("original_width")
        val originalWidth: Int,
        @SerializedName("original_height")
        val originalHeight: Int,
        @SerializedName("links")
        val links: Links
    ) : GalleryCloud {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(id, width, height, preview, original, links)
    }

    interface Mapper<T> {
        fun map(
            id: Int,
            width: Int,
            height: Int,
            preview: String,
            original: String?,
            links: Links
        ): T


        class Base : Mapper<GalleryData> {
            override fun map(
                id: Int,
                width: Int,
                height: Int,
                preview: String,
                original: String?,
                links: Links
            ): GalleryData.Base =
                GalleryData.Base(id, width, height, preview, original ?: "", links)
        }

    }
}