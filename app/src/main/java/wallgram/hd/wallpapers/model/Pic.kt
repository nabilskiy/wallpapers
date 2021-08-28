package wallgram.hd.wallpapers.model

data class Pic(

        val id: Int,
        val category: Category?,
        val user: User? = null,
        val meta: PicMeta,
        val promoted: Int,
        val published: String,
        val tags: Array<Tag>,
        val width: Int,
        val height: Int,
        val focus: FloatArray,
        val links: Links

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pic

        if (id != other.id) return false
        if (category != other.category) return false
        if (user != other.user) return false
        if (meta != other.meta) return false
        if (promoted != other.promoted) return false
        if (published != other.published) return false
        if (!tags.contentEquals(other.tags)) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (!focus.contentEquals(other.focus)) return false
        if (links != other.links) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (user?.hashCode() ?: 0)
        result = 31 * result + meta.hashCode()
        result = 31 * result + promoted
        result = 31 * result + published.hashCode()
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + focus.contentHashCode()
        result = 31 * result + links.hashCode()
        return result
    }

}
