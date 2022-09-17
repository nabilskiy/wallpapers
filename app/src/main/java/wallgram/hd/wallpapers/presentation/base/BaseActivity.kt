package wallgram.hd.wallpapers.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState
import wallgram.hd.wallpapers.util.localization.LocalizationActivityDelegate
import wallgram.hd.wallpapers.util.localization.OnLocaleChangedListener
import wallgram.hd.wallpapers.util.update.UpdateListener
import wallgram.hd.wallpapers.util.update.UpdateManager
import wallgram.hd.wallpapers.util.update.UpdateType
import java.util.*

private typealias ActivityViewBindingInflater<B> = (
    inflater: LayoutInflater
) -> B

abstract class BaseActivity<B : ViewBinding>(private val bindingInflater: ActivityViewBindingInflater<B>)
    :AppCompatActivity(), OnLocaleChangedListener{

    lateinit var binding: B

    private lateinit var updateManager : UpdateManager

    private val localizationDelegate: LocalizationActivityDelegate by lazy{
        LocalizationActivityDelegate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        initializeUpdateManager()
    }

    override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getBaseContext() = localizationDelegate.getApplicationContext(super.getBaseContext())

    override fun getApplicationContext() = localizationDelegate.getApplicationContext(super.getApplicationContext())

    override fun getResources() = localizationDelegate.getResources(super.getResources())

    override fun onBeforeLocaleChanged() {
    }

    override fun onAfterLocaleChanged() {
    }

    fun setLanguage(language: String){
        localizationDelegate.setLanguage(this, language)
    }

    fun setLanguage(language: String, country: String){
        localizationDelegate.setLanguage(this, language, country)
    }

    fun setLanguage(locale: Locale) {
        localizationDelegate.setLanguage(this, locale)
    }

    fun getCurrentLanguage() = localizationDelegate.getLanguage(this)

    private fun initializeUpdateManager(){
        val builder =
            UpdateManager.Builder()
                .setActivity(this)
                .setUpdateType(UpdateType.FLEXIBLE)
        updateManager = builder.create()
        updateManager.updateListener = object : UpdateListener {
            override fun onUpdateChecked(appUpdateInfo: AppUpdateInfo, updateAvailable: Boolean) {
                onUpdateAvailable(appUpdateInfo,updateAvailable)
            }

            override fun onUpdateCheckFailure(exception: Exception) {
                onUpdateFailure(exception)
            }

            override fun onUpdateState(installState: InstallState, bytesDownLoaded: Long, totalBytesToDownLoaded: Long) {
                onInstallState(installState, bytesDownLoaded, totalBytesToDownLoaded)
            }
        }
    }

    fun checkUpdate(){
        updateManager.checkUpdate()
    }

    protected fun startUpdate(appUpdateInfo: AppUpdateInfo){
        updateManager.update(appUpdateInfo)
    }

    protected fun restart(){
        updateManager.completeUpdate()
    }

    protected fun showRestartSnackBar(@ColorRes colorRes : Int, updateMessage : String){
        updateManager.showSnackBarForCompleteUpdate(updateMessage,
            ContextCompat.getColor(this, colorRes))
    }

    abstract fun onInstallState(installState: InstallState, bytesDownLoaded: Long, totalBytesToDownLoaded: Long)

    abstract fun onUpdateAvailable(appUpdateInfo: AppUpdateInfo,updateAvailable: Boolean)

    abstract fun onUpdateFailure(exception: Exception?)
}