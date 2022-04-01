package wallgram.hd.wallpapers.domain.tags

import wallgram.hd.wallpapers.core.ExceptionType
import wallgram.hd.wallpapers.core.Same
import wallgram.hd.wallpapers.domain.categories.Show
import wallgram.hd.wallpapers.presentation.categories.CategoryUi

sealed class TagItem {
    abstract fun <T> map(mapper: TagItemUiMapper<T>): T
    class Base(private val id: Int, private val name: String, private val count: Int, private val url: String) : TagItem() {
        override fun <T> map(mapper: TagItemUiMapper<T>) = mapper.map(id, name, count, url)
    }

    class Error(private val exceptionType: ExceptionType = ExceptionType.GENERIC) : TagItem() {
        override fun <T> map(mapper: TagItemUiMapper<T>) = mapper.map(exceptionType)
    }
}

interface TagItemUiMapper<T> {

    fun map(id: Int, name: String, count: Int, url: String): T

    fun map(exceptionType: ExceptionType): T

    class Id(private val id: Int) : TagItemUiMapper<Boolean> {
        override fun map(id: Int, name: String, count: Int, url: String) = this.id == id
        override fun map(exceptionType: ExceptionType) = false
    }

    class Display(private val show: Show<Pair<Int, String>>) : TagItemUiMapper<Unit> {
        override fun map(id: Int, name: String, count: Int, url: String) = show.open(Pair(id, name))
        override fun map(exceptionType: ExceptionType) = Unit
    }

    interface Compare : TagItemUiMapper<Boolean>, Same<CategoryUi> {

        class Base : Compare {
            private var itemToCompare: CategoryUi = CategoryUi.Empty

            override fun map(id: Int, text: String, url: String): Boolean =
                itemToCompare.map(Id(id))

            override fun map(exceptionType: ExceptionType) = false

            override fun itemToCompare(item: CategoryUi) {
                itemToCompare = item
            }
        }
    }




}