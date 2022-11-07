package wallgram.hd.wallpapers.data.resolution

import javax.inject.Inject

interface ResolutionsDataSource {

    fun resolutions(): List<String>

    class Base @Inject constructor() : ResolutionsDataSource {
        override fun resolutions(): List<String> {
            return listOf(
                "1080x1920",
                "1080x2400",
                "1080x2340",
                "1080x2160",
                "720x1280",
                "1080x2220",
                "720x1480"
            )
        }
    }
}