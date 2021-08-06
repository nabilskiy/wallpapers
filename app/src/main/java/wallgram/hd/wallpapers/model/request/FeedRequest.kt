package wallgram.hd.wallpapers.model.request

import wallgram.hd.wallpapers.ui.wallpapers.WallType

data class FeedRequest(
        var type: WallType,
        var category: Int = 0,
        var sort: String = "date",
        var page: Int = 1,
        var resolution: String = "1280x720",
        var lang: String = "en",
        var search: String = ""
)
