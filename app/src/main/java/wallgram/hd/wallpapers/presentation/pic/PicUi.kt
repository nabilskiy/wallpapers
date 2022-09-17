package wallgram.hd.wallpapers.presentation.pic

import wallgram.hd.wallpapers.data.filters.FiltersCloud
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.model.PicMeta
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.model.User
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView

class PicUi(
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
) : ItemUi {

    override fun type(): Int = 1

    override fun show(vararg views: MyView) {
        views[0].show(user?.name ?: "")
        views[1].show(meta.author.toString())
        //views[1].show(name)
//        views[0].show(text)
//        views[1].check(isFavorite)
//        views[1].handleClick {
//            changeFavorite.changeFavorite(id)
//        }
    }

    override fun id(): String = id.toString()

    override fun content(): String = id.toString()
}