package wallgram.hd.wallpapers.presentation.main

import com.google.firebase.analytics.FirebaseAnalytics
import wallgram.hd.wallpapers.R
import android.widget.Toast
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.analytics.ktx.analytics
import wallgram.hd.wallpapers.databinding.ActivityMainBinding
import wallgram.hd.wallpapers.presentation.base.BaseActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.util.modo.*
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.data.ads.appopen.OnShowAdCompleteListener
import wallgram.hd.wallpapers.data.ads.banner.BannerAd
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.wallpaper.DownloadsCountStore
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.util.modo.multi.StackContainerFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val modo = App.modo

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore

    private val viewModel: MainViewModel by viewModels()


    private val modoRender by lazy {
        object : ModoRender(this@MainActivity, R.id.container) {

            override fun createMultiStackFragment(multiScreen: MultiScreen): MultiStackFragment =
                MainFragment()
        }
    }

    private fun exitFromApp() {
        if (back_pressed + 2000 > System.currentTimeMillis()) modo.exit() else Toast.makeText(
            baseContext, resources.getString(R.string.exit_toast),
            Toast.LENGTH_SHORT
        ).show()
        back_pressed = System.currentTimeMillis()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        checkUpdate()


        firebaseAnalytics = Firebase.analytics
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                if (deepLink != null) Toast.makeText(
                    this@MainActivity,
                    deepLink.path,
                    Toast.LENGTH_SHORT
                ).show()
                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...

                // ...
            }
            .addOnFailureListener(this) { e ->
                Toast.makeText(
                    this@MainActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        viewModel.observe(this) {
            modo.init(savedInstanceState, it.screen())
        }

        viewModel.observeUpdate(this) {
            Log.d("MAIN_ACTIVITY", it.toString())
        }

    }

    override fun onUpdateAvailable(appUpdateInfo: AppUpdateInfo, updateAvailable: Boolean) {
        if (updateAvailable) {
            startUpdate(appUpdateInfo)
        }
    }

    override fun onInstallState(
        installState: InstallState,
        bytesDownLoaded: Long,
        totalBytesToDownLoaded: Long
    ) {
        when (installState.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                restart()
            }
        }
    }


    override fun onUpdateFailure(exception: Exception?) {
        Toast.makeText(this, exception?.message ?: "", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        modo.render = modoRender
    }


    override fun onPause() {
        modo.render = null
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        modo.saveState(outState)
    }

    private fun getCurrentFragment() =
        supportFragmentManager.fragments
            .filterIsInstance<WallpaperFragment>()
            .firstOrNull { it.isVisible }


    override fun onBackPressed() {
        val screen =
            if ((modoRender.currentState.chain.isNotEmpty())) modoRender.currentState.chain[modoRender.currentState.chain.lastIndex] else null
        screen?.let {
            if (screen is MultiScreen) {
                if (screen.stacks[screen.selectedStack].chain.size == 1)
                    if (screen.selectedStack != 0) {
                        modo.selectStack(0)
                    } else exitFromApp()
                else {
                    back()
                }
            } else {
                back()
            }
        }
    }

    private fun back() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is WallpaperFragment) {
            currentFragment.back()
        } else viewModel.back()
    }

    companion object {
        private var back_pressed: Long = 0
    }
}