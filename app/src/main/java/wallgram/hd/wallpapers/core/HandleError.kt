package wallgram.hd.wallpapers.core

interface HandleError {

    fun handle(error: Exception): Exception
}