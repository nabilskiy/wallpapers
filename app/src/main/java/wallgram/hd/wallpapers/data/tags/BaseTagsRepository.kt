package wallgram.hd.wallpapers.data.tags

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import wallgram.hd.wallpapers.data.core.AbstractRepository
import wallgram.hd.wallpapers.data.core.ServerResponse
import wallgram.hd.wallpapers.data.remote.service.AkspicService
import wallgram.hd.wallpapers.util.DisplayProvider
import wallgram.hd.wallpapers.util.localization.LocalizationApplicationDelegate
import wallgram.hd.wallpapers.util.stringSuspending
import javax.inject.Inject

class BaseTagsRepository @Inject constructor(
    private val service: AkspicService,
    displayProvider: DisplayProvider,
    localeProvider: LocalizationApplicationDelegate
) : AbstractRepository(displayProvider, localeProvider), TagsRepository {

    private val gson = Gson()
    private val type = object : TypeToken<ServerResponse<Tag>>() {}.type

    private val dataList = ArrayList<Tag>()

    override fun getCachedData() = dataList

    override suspend fun getData(): List<Tag> {
        val cachedList = getCachedData()

        if (cachedList.isEmpty()) {
            val data = service.getTags(1, 1, fetchLanguage())
            val list: ServerResponse<Tag> = gson.fromJson(data.stringSuspending(), type)

            if (list.list.isNotEmpty()) {
                dataList.addAll(list.list)
            }

        }
        return cachedList
    }

}