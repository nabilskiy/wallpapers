package wallgram.hd.wallpapers.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Links(
        val portrait: String? = null,
        val landscape: String? = null,
        val source: String
): Parcelable
