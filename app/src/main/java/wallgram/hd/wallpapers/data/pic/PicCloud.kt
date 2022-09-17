package wallgram.hd.wallpapers.data.pic

import com.google.gson.annotations.SerializedName
import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.domain.pic.PicDomain
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.model.PicMeta
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.model.User

interface PicCloud {

    fun <T> map(mapper: Mapper<T>): T

    class Empty : PicCloud {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(
                -1,
                null,
                null,
                PicMeta(),
                0,
                "",
                listOf(),
                0,
                0,
                listOf(),
                Links("", "", "")
            )
    }

    data class Base(
        @SerializedName("id")
        val id: Int,
        @SerializedName("category")
        val category: FiltersCloud.Base? = null,
        @SerializedName("user")
        val user: User? = null,
        @SerializedName("meta")
        val meta: PicMeta,
        @SerializedName("promoted")
        val promoted: Int,
        @SerializedName("published")
        val published: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("width")
        val width: Int,
        @SerializedName("height")
        val height: Int,
        @SerializedName("focus")
        val focus: List<Float>,
        @SerializedName("links")
        val links: Links
    ) : PicCloud {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(
                id,
                category,
                user,
                meta,
                promoted,
                published,
                tags,
                width,
                height,
                focus,
                links
            )
    }

    interface Mapper<T> {
        fun map(
            id: Int,
            category: FiltersCloud.Base?,
            user: User? = null,
            meta: PicMeta,
            promoted: Int,
            published: String,
            tags: List<Tag>,
            width: Int,
            height: Int,
            focus: List<Float>,
            links: Links
        ): T

        class Base : Mapper<PicDomain> {
            override fun map(
                id: Int,
                category: FiltersCloud.Base?,
                user: User?,
                meta: PicMeta,
                promoted: Int,
                published: String,
                tags: List<Tag>,
                width: Int,
                height: Int,
                focus: List<Float>,
                links: Links
            ) =
                PicDomain.Base(
                    id,
                    category,
                    user,
                    meta,
                    promoted,
                    published,
                    tags,
                    width,
                    height,
                    focus,
                    links
                )
        }
    }
}