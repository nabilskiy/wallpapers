package wallgram.hd.wallpapers.model

import com.google.gson.annotations.SerializedName

data class PicMeta(
        val license: String? = null,
        val author: String? = null,
        @SerializedName("author-source")
        val author_source: String? = null
)
