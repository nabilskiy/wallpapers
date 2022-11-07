package wallgram.hd.wallpapers.views.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.Coil
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.util.CoilUtils

class CoilProvider(context: Context, private val paths: Array<String>) : ImageProvider {

    private val loader = Coil.imageLoader(context)

    override fun onProvider(position: Int): Bitmap? {
        try {
            val bitmap: Bitmap?
            if (position >= 0 && position < paths.size) {
                bitmap = loader.memoryCache?.get(MemoryCache.Key(paths[position]))?.bitmap
                val t = loader.memoryCache?.get(MemoryCache.Key(""))?.toString()
                return bitmap
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }

    override fun keys() = paths.toList()
}