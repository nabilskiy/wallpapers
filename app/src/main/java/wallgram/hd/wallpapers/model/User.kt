package wallgram.hd.wallpapers.model

data class User(
        val id: Int,
        val nick: String,
        val name: String? = null,
        val avatar: String? = null
)
