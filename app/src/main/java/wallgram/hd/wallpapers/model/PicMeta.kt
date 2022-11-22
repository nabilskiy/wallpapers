package wallgram.hd.wallpapers.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PicMeta(
    @SerializedName("license")
    val license: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("author-source")
    val author_source: String? = null
)
