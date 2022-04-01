package wallgram.hd.wallpapers.domain.tags

import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.data.categories.Category
import wallgram.hd.wallpapers.data.tags.Tag

/**
 * Мапим данные с сервера к данным бизнес логики
 **/

class TagItemMapper : Mapper<List<TagItem>, List<Tag>> {

    override fun map(source: List<Tag>) = source.map {
        TagItem.Base(
            it.id,
            it.name,
            it.total,
            it.background
        )
    }
}