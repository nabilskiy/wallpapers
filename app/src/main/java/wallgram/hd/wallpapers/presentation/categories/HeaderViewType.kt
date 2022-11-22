package wallgram.hd.wallpapers.presentation.categories

interface HeaderViewType {

    fun textSize(): Float

    class Default : HeaderViewType {
        override fun textSize() = 28f
    }

    class Small : HeaderViewType {
        override fun textSize() = 22f
    }

}