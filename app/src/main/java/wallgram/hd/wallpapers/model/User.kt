package wallgram.hd.wallpapers.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
        val id: Int,
        val nick: String,
        val name: String? = null,
        val avatar: String? = null
)
