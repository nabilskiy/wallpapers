package wallgram.hd.wallpapers.model

data class Suggest(
        val status: Int,
        val data: List<String>,
        val errors: List<Any>
)
