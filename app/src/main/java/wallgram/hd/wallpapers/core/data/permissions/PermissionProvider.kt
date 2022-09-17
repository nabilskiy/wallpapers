package wallgram.hd.wallpapers.core.data.permissions

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

interface PermissionProvider {

    fun rationale(description: String): PermissionProvider
    fun request(vararg permissions: Permission): PermissionProvider
    fun checkPermission(callback: (Boolean) -> Unit)
    fun checkDetailedPermission(callback: (Map<Permission, Boolean>) -> Unit)

    class Base(f: Fragment) : PermissionProvider {

        private val fragment: WeakReference<Fragment> = WeakReference(f)

        private val requiredPermissions = mutableListOf<Permission>()
        private var rationale: String? = null
        private var callback: (Boolean) -> Unit = {}
        private var detailedCallback: (Map<Permission, Boolean>) -> Unit = {}

        private val permissionCheck = fragment.get()
            ?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResult ->
                sendResultAndCleanUp(grantResult)
            }

        override fun rationale(description: String) = apply {
            rationale = description
        }

        override fun request(vararg permissions: Permission) = apply {
            requiredPermissions.addAll(permissions)
        }

        override fun checkPermission(callback: (Boolean) -> Unit) {
            this.callback = callback
            handlePermissionRequest()
        }

        override fun checkDetailedPermission(callback: (Map<Permission, Boolean>) -> Unit) {
            this.detailedCallback = callback
            handlePermissionRequest()
        }

        private fun handlePermissionRequest() {
            fragment.get()?.let { fragment ->
                when {
                    areAllPermissionsGranted(fragment) -> sendPositiveResult()
                    shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
                    else -> requestPermissions()
                }

            }
        }

        private fun displayRationale(fragment: Fragment) {
            AlertDialog.Builder(fragment.requireContext())
                .setTitle("")
                .setMessage(rationale ?: "")
                .setCancelable(false)
                .setPositiveButton("") { _, _ ->
                    requestPermissions()
                }.show()
        }

        private fun sendPositiveResult() {
            sendResultAndCleanUp(getPermissionList().associateWith { true })
        }

        private fun sendResultAndCleanUp(grantResult: Map<String, Boolean>) {
            callback(grantResult.all { it.value })
            detailedCallback(grantResult.mapKeys { Permission.from(it.key) })
            cleanUp()
        }

        private fun cleanUp() {
            requiredPermissions.clear()
            rationale = null
            callback = {}
            detailedCallback = {}
        }

        private fun requestPermissions() {
            permissionCheck?.launch(getPermissionList())
        }

        private fun areAllPermissionsGranted(fragment: Fragment) = requiredPermissions.all {
            it.isGranted(fragment)
        }

        private fun shouldShowPermissionRationale(fragment: Fragment) =
            requiredPermissions.any { it.requiresRationale(fragment) }

        private fun getPermissionList() =
            requiredPermissions.flatMap { it.permissions().toList() }.toTypedArray()

        private fun Permission.isGranted(fragment: Fragment) =
            permissions().all { hasPermission(fragment, it) }

        private fun Permission.requiresRationale(fragment: Fragment) =
            permissions().any { fragment.shouldShowRequestPermissionRationale(it) }

        private fun hasPermission(fragment: Fragment, permission: String) =
            ContextCompat.checkSelfPermission(
                fragment.requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED
    }


}