package wallgram.hd.wallpapers.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import retrofit2.Response
import wallgram.hd.wallpapers.data.remote.ServiceGenerator
import wallgram.hd.wallpapers.data.remote.service.AkspicService
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.ServerResponse
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import java.io.IOException

private const val PAGE_START = 1

class FeedPagingSource(var feedRequest: FeedRequest,
                       val serviceGenerator: ServiceGenerator
) : PagingSource<Int, Gallery>() {


    private val service = serviceGenerator.createService(AkspicService::class.java)

    override fun getRefreshKey(state: PagingState<Int, Gallery>): Int? = state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery> {
        val position = params.key ?: PAGE_START
        try {

            val response: Response<ServerResponse<Gallery>> = when(feedRequest.type){
                WallType.CATEGORY -> service.getWallpapersFromCategory(feedRequest.category, feedRequest.sort, position, feedRequest.resolution, feedRequest.lang)
                WallType.TAG -> service.getWallpapersFromTag(feedRequest.category, feedRequest.sort, position, feedRequest.resolution, feedRequest.lang)
                WallType.SEARCH -> service.search(feedRequest.search, position, feedRequest.resolution, feedRequest.lang)
                WallType.SIMILAR -> service.getSimilar(feedRequest.category, feedRequest.resolution, feedRequest.lang, position)
                WallType.COLOR -> service.getWallpapersItems(feedRequest.sort, position, feedRequest.resolution, feedRequest.lang, r = feedRequest.r, g = feedRequest.g, b = feedRequest.b)
                else -> service.getWallpapersItems(feedRequest.sort, position, feedRequest.resolution, feedRequest.lang)
            }

            val feed = response.body()?.list ?: listOf()

            return LoadResult.Page(
                    data = feed,
                    prevKey = if (position == PAGE_START) null else position - 1,
                    nextKey = if (feed.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}