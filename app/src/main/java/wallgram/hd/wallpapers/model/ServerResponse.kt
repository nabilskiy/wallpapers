package wallgram.hd.wallpapers.model

data class ServerResponse<T : Any>(
        val list: List<T>,
        val seed: Int? = null
)