package wallgram.hd.wallpapers.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerResponse<T : Any>(
        val list: List<T>,
        val seed: Int? = null
)
