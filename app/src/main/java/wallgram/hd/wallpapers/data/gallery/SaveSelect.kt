package wallgram.hd.wallpapers.data.gallery

import coil.memory.MemoryCache

interface SaveSelect {

    fun save(id: Int, requestId: String)

}