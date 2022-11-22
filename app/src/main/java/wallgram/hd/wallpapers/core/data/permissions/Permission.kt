package wallgram.hd.wallpapers.core.data.permissions

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi

interface Permission {
    fun permissions(): Array<out String>

    abstract class Abstract(private vararg val permissions: String) : Permission {
        override fun permissions() = permissions
    }

    object Camera : Abstract(CAMERA)
    object Location : Abstract(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    object Test : Abstract(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    object Storage : Permission {
        override fun permissions() = if (isNewApi()) tiramisuPermissions() else basePermission()

        @RequiresApi(33)
        private fun tiramisuPermissions() = arrayOf(READ_MEDIA_IMAGES)

        private fun basePermission() = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

        private fun isNewApi() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    companion object {
        fun from(permission: String): Permission = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            READ_MEDIA_IMAGES -> Storage
            MANAGE_EXTERNAL_STORAGE -> Storage
            CAMERA -> Camera
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}