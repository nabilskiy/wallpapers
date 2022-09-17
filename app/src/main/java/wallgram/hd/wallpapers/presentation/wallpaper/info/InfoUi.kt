package wallgram.hd.wallpapers.presentation.wallpaper.info

import android.view.View
import android.widget.TextView
import android.widget.Toast
import wallgram.hd.wallpapers.model.Tag
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.adapter.MyView
import wallgram.hd.wallpapers.util.linktextview.Linker
import wallgram.hd.wallpapers.util.linktextview.internal.OnLinkClickListener

class InfoUi(
    private val id: Int,
    private val title: String,
    private val value: String,
    private val navigateTag: NavigateTag
) : ItemUi {

    override fun type(): Int = 9

    override fun show(vararg views: MyView) {
        views[0].show(title)
        views[1].show(value)

        if (id == 2) {
            views[1].handleClick {
                navigateTag.navigate(value)
            }
        }
    }

    override fun id(): String = id.toString()

    override fun content(): String = id.toString()
}

class TagsUi(
    private val id: Int,
    private val title: String,
    private val value: List<Tag>,
    private val navigateTag: NavigateTag
) : ItemUi {

    override fun type(): Int = 13

    override fun show(vararg views: MyView) {
        val map = value.associate { Pair(it.name, it.id) }
        val content = value.joinToString(", ") { it.name }
        Linker.Builder()
            .content(content)
            .textView(views[0] as TextView)
            .links(content.split(", "))
            .addOnLinkClickListener(object : OnLinkClickListener {
                override fun onClick(view: View, content: String) {
                    val item = map[content] ?: 0
                    navigateTag.navigate(Pair(item, content))
                    //   Toast.makeText(view.context, item.toString(), Toast.LENGTH_SHORT).show()
                }

            })
            .apply()
    }

    override fun id(): String = id.toString()

    override fun content(): String = id.toString()
}