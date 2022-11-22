package wallgram.hd.wallpapers.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Tag(
    @SerializedName("id")
    var id: Int = -1,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("total")
    val total: Int? = 0,
    @SerializedName("background")
    val image: String? = null
) : Parcelable