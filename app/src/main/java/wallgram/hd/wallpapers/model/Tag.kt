package wallgram.hd.wallpapers.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
        var id: Int = -1,
        val name: String = "",
        val total: Int? = 0,
        @SerializedName("background")
        val image: String? = null
): Parcelable