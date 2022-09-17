package wallgram.hd.wallpapers.presentation.base

import androidx.annotation.StringRes
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ResourceProvider
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.core.domain.NoInternetConnectionException
import wallgram.hd.wallpapers.core.domain.ServiceUnavailableException

abstract class HandleUiErrorAbstract(
    private val manageResources: ResourceProvider,
    private val globalErrorCommunication: GlobalErrorCommunication.Update,
    private val handleError: HandleError =
        HandleGenericUiError(manageResources, globalErrorCommunication)
) : HandleError {

    @StringRes
    protected open val noConnectionExceptionMessage: Int = R.string.connection_error

    @StringRes
    protected open val serviceUnavailableExceptionMessage: Int = R.string.connection_error

    override fun handle(error: Exception): Exception {
        val messageId = when (error) {
            is NoInternetConnectionException -> noConnectionExceptionMessage
            is ServiceUnavailableException -> serviceUnavailableExceptionMessage
            else -> 0
        }
        return if (messageId == 0)
            handleError.handle(error)
        else {
            globalErrorCommunication.map(manageResources.string(messageId))
            error
        }
    }
}

class HandleGenericUiError(
    private val manageResources: ResourceProvider,
    private val globalErrorCommunication: GlobalErrorCommunication.Update
) : HandleError {

    override fun handle(error: Exception): Exception {
        globalErrorCommunication.map(manageResources.string(R.string.connection_error))
        return error
    }
}

class HandleUiError(
    manageResources: ResourceProvider,
    globalErrorCommunication: GlobalErrorCommunication.Update
) : HandleUiErrorAbstract(manageResources, globalErrorCommunication)