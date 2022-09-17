package wallgram.hd.wallpapers.domain.filters


interface CategoriesRepository {
    suspend fun filters(): CategoriesDomain
}