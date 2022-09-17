package wallgram.hd.wallpapers.core.domain

import wallgram.hd.wallpapers.core.HandleError
import java.net.UnknownHostException
import javax.inject.Inject

class HandleDomainError @Inject constructor() : HandleError {

    override fun handle(error: Exception) =
        if (error is UnknownHostException)
            NoInternetConnectionException()
        else
            ServiceUnavailableException()
}
