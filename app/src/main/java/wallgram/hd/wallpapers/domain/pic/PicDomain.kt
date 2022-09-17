package wallgram.hd.wallpapers.domain.pic

import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.model.PicMeta
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.model.User
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.presentation.pic.PicUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.InfoListUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.InfoUi
import wallgram.hd.wallpapers.presentation.wallpaper.info.NavigateTag
import wallgram.hd.wallpapers.presentation.wallpaper.info.TagsUi

interface PicDomain {

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val id: Int,
        private val category: FiltersCloud.Base?,
        private val user: User? = null,
        private val meta: PicMeta,
        private val promoted: Int,
        private val published: String,
        private val tags: List<Tag>,
        private val width: Int,
        private val height: Int,
        private val focus: List<Float>,
        private val links: Links
    ) :
        PicDomain {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(
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
            user: User?,
            meta: PicMeta,
            promoted: Int,
            published: String,
            tags: List<Tag>,
            width: Int,
            height: Int,
            focus: List<Float>,
            links: Links
        ): T

        class Base : Mapper<PicUi> {

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
                PicUi(
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

        class Info(
            private val navigateTag: NavigateTag
        ) : Mapper<InfoListUi> {
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
            ): InfoListUi {
                val result = mutableListOf<ItemUi>()

                val date = published
                val resolution = width.toString() + "x" + height.toString()

                result.add(TagsUi(0, "", tags, navigateTag))

                if (!meta.author.isNullOrEmpty())
                    result.add(InfoUi(1, "Автор", meta.author, navigateTag))
                else if (user != null)
                    result.add(InfoUi(1, "Автор", user.nick, navigateTag))

                if (!meta.author_source.isNullOrEmpty())
                    result.add(InfoUi(2, "Источник", meta.author_source, navigateTag))

                if (!meta.license.isNullOrEmpty())
                    result.add(InfoUi(3, "Лицензия", meta.license, navigateTag))

                result.add(InfoUi(4, "Дата", date, navigateTag))
                result.add(InfoUi(5, "Разрешение", resolution, navigateTag))

                return InfoListUi.Base(result)
            }

        }
    }

}