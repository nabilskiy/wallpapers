package wallgram.hd.wallpapers.presentation.base.views.coil.transform

import android.graphics.*
import coil.size.Size
import coil.transform.Transformation

class ColorFilterTransformation(private val color: Int) : Transformation {

    override val cacheKey = "${javaClass.name}-$color"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val canvas = Canvas(input)
        val paint = Paint(color)
        val filter = LightingColorFilter(color, 0x000000)
        paint.colorFilter = filter
        canvas.drawBitmap(input, Matrix(), paint)

        return input
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ColorFilterTransformation

        if (color != other.color) return false

        return true
    }

    override fun hashCode() = color


}