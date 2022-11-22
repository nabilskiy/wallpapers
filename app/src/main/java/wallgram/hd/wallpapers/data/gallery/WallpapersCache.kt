package wallgram.hd.wallpapers.data.gallery

import wallgram.hd.wallpapers.core.Update
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

interface WallpapersCache {

    interface Save : wallgram.hd.wallpapers.core.Save<WallpaperCache>
    interface Read : wallgram.hd.wallpapers.core.Read<WallpaperCache>
    interface Remove : wallgram.hd.wallpapers.core.Remove<WallpaperCache>
    //  interface Update : wallgram.hd.wallpapers.core.Update<WallpaperCache>

    interface Mutable : Save, Read, Remove

    class Base @Inject constructor() : Mutable {

        private val deque: LinkedList<WallpaperCache> = LinkedList()

        override fun save(data: WallpaperCache) {
            deque.addLast(data)
        }

        override fun read(): WallpaperCache = deque.peekLast() ?: WallpaperCache.Empty()

        override fun removeLast() {
            deque.pollLast()
        }

    }
}
