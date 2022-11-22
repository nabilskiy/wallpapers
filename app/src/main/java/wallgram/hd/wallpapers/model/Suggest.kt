package wallgram.hd.wallpapers.model

import androidx.annotation.Keep

@Keep
data class Suggest(
    val status: Int,
    val data: List<String>,
    val errors: List<Any>
)
