package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.DisplayProvider
import javax.inject.Inject

interface ResolutionsDataSource {

    fun resolutions(): Resolution

    class Base @Inject constructor(

    ) : ResolutionsDataSource {
        override fun resolutions(): Resolution {
            val list = mutableListOf(
                "1080x1920",
                "1080x2400",
                "1080x2340",
                "1080x2160",
                "720x1280",
                "1080x2220",
                "720x1480"
            )

            return Resolution.Base(list)
        }


    }
}