package wallgram.hd.wallpapers.data.error.mapper

import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.error.*
import javax.inject.Inject

class ErrorMapper @Inject constructor() : ErrorMapperInterface {

    override fun getErrorString(errorId: Int): String {
        return wallgram.hd.wallpapers.App.context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
            Pair(NETWORK_ERROR, getErrorString(R.string.network_error)),
            Pair(PASS_WORD_ERROR, getErrorString(R.string.invalid_password)),
            Pair(USER_NAME_ERROR, getErrorString(R.string.invalid_username)),
            Pair(CHECK_YOUR_FIELDS, getErrorString(R.string.invalid_username_and_password)),
            Pair(SEARCH_ERROR, getErrorString(R.string.search_error)),
            Pair(PASS_CONFIRM_ERROR, getErrorString(R.string.password_confirm_error)),
            Pair(CONFLICT, getErrorString(R.string.conflict)),
            Pair(INVALID_CREDENTIALS, getErrorString(R.string.invalid_credentials)),
            Pair(FIRST_NAME_ERROR, getErrorString(R.string.fill_field)),
            Pair(LAST_NAME_ERROR, getErrorString(R.string.fill_field)),
            Pair(PHONE_NUMBER_ERROR, getErrorString(R.string.fill_field)),
            Pair(SUCCESSFULLY, getErrorString(R.string.user_update_successfully))
        ).withDefault { getErrorString(R.string.network_error) }
}
