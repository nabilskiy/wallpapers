package wallgram.hd.wallpapers.data.tags

import wallgram.hd.wallpapers.data.categories.Category

interface TagsRepository {

    /**
     * @return список сырых данных напрямую из локального кеша
     */
    fun getCachedData() : ArrayList<Tag>

    /**
     * @return данные от сервера
     */
    suspend fun getData(): List<Tag>

}