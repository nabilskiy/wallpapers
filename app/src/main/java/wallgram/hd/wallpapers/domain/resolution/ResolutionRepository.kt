package wallgram.hd.wallpapers.domain.resolution

import wallgram.hd.wallpapers.data.resolution.ChangeResolution
import wallgram.hd.wallpapers.data.resolution.Resolution

interface ResolutionRepository {
    fun resolutions(): ResolutionsDomain
    fun currentResolution(): String
}