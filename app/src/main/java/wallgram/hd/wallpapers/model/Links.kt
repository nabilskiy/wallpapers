package wallgram.hd.wallpapers.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Links(
        val portrait: String? = null,
        val landscape: String? = null,
        val source: String
): Parcelable
