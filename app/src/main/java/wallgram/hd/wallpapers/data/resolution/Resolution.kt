package wallgram.hd.wallpapers.data.resolution

import wallgram.hd.wallpapers.domain.resolution.ResolutionsDomain

interface Resolution {

    fun <T> map(mapper: Mapper<T>): T

    data class Base(
        private val resolutions: List<String>
    ) : Resolution {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(resolutions)

    }

    interface Mapper<T> {
        fun map(resolutions: List<String>): T

        class Base : Mapper<ResolutionsDomain> {
            override fun map(resolutions: List<String>): ResolutionsDomain =
                ResolutionsDomain.Base(resolutions)

        }
    }
}