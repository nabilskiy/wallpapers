package wallgram.hd.wallpapers.core.data

interface MakeService {

    fun <T> service(clasz: Class<T>): T

    abstract class Abstract(
        private val retrofitBuilder: ProvideRetrofitBuilder,
    ) : MakeService {

        private val retrofit by lazy {
            retrofitBuilder.provideRetrofitBuilder()
                .baseUrl(baseUrl())
                .build()
        }

        override fun <T> service(clasz: Class<T>): T = retrofit.create(clasz)

        private fun baseUrl(): String = "https://wallspic.com/rest/v3/"
    }
}