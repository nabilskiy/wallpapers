package wallgram.hd.wallpapers.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PicMeta(
        val license: String? = null,
        val author: String? = null,
        @Json(name = "author-source")
        val author_source: String? = null
)
