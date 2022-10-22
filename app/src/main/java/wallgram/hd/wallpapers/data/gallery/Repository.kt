package wallgram.hd.wallpapers.data.gallery

interface Repository<T> {
    suspend fun fetchData(): T
}