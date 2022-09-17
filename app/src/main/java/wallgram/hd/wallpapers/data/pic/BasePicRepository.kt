package wallgram.hd.wallpapers.data.pic

import wallgram.hd.wallpapers.domain.pic.PicDomain
import wallgram.hd.wallpapers.domain.pic.PicRepository
import javax.inject.Inject

class BasePicRepository @Inject constructor(
    private val cloudDataSource: PicCloudDataSource,
    private val mapper: PicCloud.Mapper<PicDomain>
) : PicRepository {

    override suspend fun pic(id: Int): PicDomain {
        val pic = cloudDataSource.pic(id)
        return pic.map(mapper)
    }

}