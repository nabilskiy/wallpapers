package wallgram.hd.wallpapers.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class SubCategory {
     open var id: Int = -1
     open val name: String= ""
     open val background: String? = null
}

