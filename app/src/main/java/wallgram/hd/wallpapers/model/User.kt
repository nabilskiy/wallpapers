package wallgram.hd.wallpapers.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nick")
    val nick: String,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null
)
