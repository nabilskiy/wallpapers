package wallgram.hd.wallpapers.data.gallery

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import wallgram.hd.wallpapers.domain.gallery.GalleryDomain
import wallgram.hd.wallpapers.model.Links


interface GalleryCache {

    fun <T> map(mapper: Mapper<T>): T

    class Empty : GalleryCache {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(-1, 1080, 1920, "", "", Links("", "", ""))
    }

    @Entity(tableName = "favorites", primaryKeys = ["id", "isHistory"])
    data class Base(
        val id: Int,
        val width: Int,
        val height: Int,
        val preview: String,
        val original: String? = null,
        val promoted: Int,
        val originalWidth: Int,
        val originalHeight: Int,
        @Embedded
        val links: Links,
        val isHistory: Boolean
    ) : GalleryCache {
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
            ): GalleryData.Base = GalleryData.Base(id, width, height, preview, original ?: "", links)
        }
    }
}