package wallgram.hd.wallpapers.domain.filters

import wallgram.hd.wallpapers.data.gallery.SaveSelect


interface CategoriesRepository: SaveSelect {
    suspend fun filters(): CategoriesDomain
}