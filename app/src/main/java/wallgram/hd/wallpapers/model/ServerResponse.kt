package wallgram.hd.wallpapers.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ServerResponse<T : Any>(
    @SerializedName("list")
    val list: List<T>,
    @SerializedName("seed")
    val seed: Int? = null
)