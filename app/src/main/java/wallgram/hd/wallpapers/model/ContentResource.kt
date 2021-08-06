package wallgram.hd.wallpapers.model

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

data class ContentResource(
        val url: String?
) {

    companion object {

        private const val URL_KEY = "url"

        /**
         * Parse subscription data from String and return null if data is not valid.
         */
        fun listFromMap(map: Map<String, Any>): ContentResource? {
            val url = map[URL_KEY] as? String ?: return null
            return ContentResource(url)
        }

        /**
         * Parse subscription data from String and return null if data is not valid.
         */
        fun fromJsonString(dataString: String): ContentResource? {
            val gson = Gson()
            return try {
                gson.fromJson(dataString, ContentResource::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

    }

}
