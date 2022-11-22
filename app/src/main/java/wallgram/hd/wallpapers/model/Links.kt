package wallgram.hd.wallpapers.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Links(
    @SerializedName("portrait")
    val portrait: String? = null,
    @SerializedName("landscape")
    val landscape: String? = null,
    @SerializedName("source")
    val source: String
) : Parcelable
