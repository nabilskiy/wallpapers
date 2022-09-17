package wallgram.hd.wallpapers.domain.pic


interface PicRepository {
    suspend fun pic(id: Int): PicDomain
}