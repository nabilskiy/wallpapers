package wallgram.hd.wallpapers.usecase.errors


interface ErrorFactory {
    fun getError(errorCode: Int): wallgram.hd.wallpapers.data.error.Error
}