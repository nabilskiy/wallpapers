package wallgram.hd.wallpapers.data.colors

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import wallgram.hd.wallpapers.R

interface Color {

    fun getId(): Int

    @StringRes
    fun getTitle(): Int

    @ColorRes
    fun getTextColor(): Int

    @ColorRes
    fun getStartColor(): Int

    @ColorRes
    fun getEndColor(): Int

    @ColorRes
    fun getColor(): Int

    fun <T> map(mapper: Mapper<T>): T

    abstract class Base(private val id: Int) : Color {
        override fun getId() = id

        override fun <T> map(mapper: Mapper<T>): T = mapper.map(
            id, getTitle(), getColor(), getTextColor(), getStartColor(), getEndColor()
        )
    }

    class Silver : Base(0) {
        override fun getTitle() = R.string.silver
        override fun getTextColor() = R.color.color_black
        override fun getStartColor() = R.color.color_silver_start
        override fun getEndColor() = R.color.color_silver_end
        override fun getColor() = R.color.color_tag_13
    }

    class Blue : Base(1) {
        override fun getTitle() = R.string.blue
        override fun getTextColor() = R.color.color_white
        override fun getStartColor() = R.color.color_blue_start
        override fun getEndColor() = R.color.color_blue_end
        override fun getColor() = R.color.color_tag_6
    }

    class Pink : Base(2) {
        override fun getTitle() = R.string.pink
        override fun getTextColor() = R.color.color_white
        override fun getStartColor() = R.color.color_pink_start
        override fun getEndColor() = R.color.color_pink_end
        override fun getColor() = R.color.color_tag_7
    }

    class Red : Base(3) {
        override fun getTitle() = R.string.red
        override fun getTextColor() = R.color.color_white
        override fun getStartColor() = R.color.color_red_start
        override fun getEndColor() = R.color.color_red_end
        override fun getColor() = R.color.color_tag_1
    }

    class Gold : Base(4) {
        override fun getTitle() = R.string.gold
        override fun getTextColor() = R.color.color_black
        override fun getStartColor() = R.color.color_gold_start
        override fun getEndColor() = R.color.color_gold_end
        override fun getColor() = R.color.color_tag_3
    }

    class White : Base(5) {
        override fun getTitle() = R.string.white
        override fun getTextColor() = R.color.color_black
        override fun getStartColor() = R.color.color_white_start
        override fun getEndColor() = R.color.color_white_end
        override fun getColor() = R.color.color_tag_11
    }

    class Black : Base(6) {
        override fun getTitle() = R.string.black
        override fun getTextColor() = R.color.color_white
        override fun getStartColor() = R.color.color_black_start
        override fun getEndColor() = R.color.color_black_end
        override fun getColor() = R.color.color_tag_10
    }

    class Green : Base(7) {
        override fun getTitle() = R.string.green
        override fun getTextColor() = R.color.color_white
        override fun getStartColor() = R.color.color_green_start
        override fun getEndColor() = R.color.color_green_end
        override fun getColor() = R.color.color_tag_4
    }

    interface Mapper<T> {
        fun map(id: Int, title: Int, color: Int, textColor: Int, startColor: Int, endColor: Int): T
    }

}