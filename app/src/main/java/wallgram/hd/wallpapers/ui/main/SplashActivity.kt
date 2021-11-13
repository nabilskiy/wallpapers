package wallgram.hd.wallpapers.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dagger.android.AndroidInjection
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.local.preference.LAUNCH_COUNT
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.data.repository.billing.BillingRepository
import wallgram.hd.wallpapers.ui.base.BaseActivity
import javax.inject.Inject

private const val COUNTER_TIME = 1L

private const val LOG_TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {

    private var secondsRemaining: Long = 0L

    @Inject
    lateinit var preferences: PreferenceContract

    @Inject
    lateinit var billingRepository: BillingRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)


        if (!valueOfLaunchCountModified) {
            var launchCount = preferences.getInt(LAUNCH_COUNT, 0)
            launchCount += 1
            preferences.save(LAUNCH_COUNT, launchCount)
            valueOfLaunchCountModified = true
        }

        billingRepository.getCurrentSub().asLiveData().observe(this@SplashActivity, {
            if(it == DEFAULT_SKU)
                createTimer(COUNTER_TIME)
            else{
                startMainActivity()
            }
        })

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

                val launchCount = preferences.getInt(LAUNCH_COUNT, 0)
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