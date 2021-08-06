package wallgram.hd.wallpapers.model

import com.squareup.moshi.Json

data class PicMeta(
        val license: String? = null,
        val author: String? = null,
        @Json(name = "author-source")
        val author_source: String? = null
)
