package wallgram.hd.wallpapers.data.error

class Error(val code: Int, val description: String) {
    constructor(exception: Exception) : this(
        code = DEFAULT_ERROR, description = exception.message
            ?: ""
    )
}

const val NO_INTERNET_CONNECTION = -1
const val NETWORK_ERROR = -2
const val DEFAULT_ERROR = -3
const val FIRST_NAME_ERROR = -106
const val LAST_NAME_ERROR = -107
const val PHONE_NUMBER_ERROR = -108
const val PASS_CONFIRM_ERROR = -105
const val PASS_WORD_ERROR = -101
const val USER_NAME_ERROR = -102
const val CHECK_YOUR_FIELDS = -103
const val SEARCH_ERROR = -104
const val CONFLICT = 409
const val INVALID_CREDENTIALS = 401

const val SUCCESSFULLY = 0