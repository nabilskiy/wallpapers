package wallgram.hd.wallpapers.presentation.main

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.data.billing.BillingRepository
import javax.inject.Inject

private const val COUNTER_TIME = 1L

private const val LOG_TAG = "SplashActivity"

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var secondsRemaining: Long = 0L

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore

    @Inject
    lateinit var billingRepository: BillingRepository

    private lateinit var appLaunchStore: AppLaunchStore.Mutable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appLaunchStore = AppLaunchStore.Base(preferenceDataStore)

        if (!valueOfLaunchCountModified) {
            var launchCount = appLaunchStore.read()
            launchCount += 1
            appLaunchStore.save(launchCount)
            valueOfLaunchCountModified = true
        }

        billingRepository.getCurrentSub().asLiveData().observe(this@SplashActivity) {
            if (it == DEFAULT_SKU)
                createTimer(COUNTER_TIME)
            else {
                startMainActivity()
            }
        }

    }


    private fun createTimer(seconds: Long) {
        val countDownTimer: CountDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000 + 1
            }

            override fun onFinish() {
                secondsRemaining = 0

                val application = application as? App

                if (application == null) {
                    startMainActivity()
                    return
                }

                val launchCount = appLaunchStore.read()
                if (launchCount == 0 || launchCount % 2 != 0) {
                    startMainActivity()
                    return
                }

                application.showAdIfAvailable(
                    this@SplashActivity,
                    object : App.OnShowAdCompleteListener {
                        override fun onShowAdComplete() {
                            startMainActivity()
                        }
                    })
            }
        }
        countDownTimer.start()
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        var valueOfLaunchCountModified = false
    }
}