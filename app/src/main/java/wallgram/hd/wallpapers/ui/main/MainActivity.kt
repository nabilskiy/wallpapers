package wallgram.hd.wallpapers.ui.main

import wallgram.hd.wallpapers.views.ConfirmationDialogFragment.ConfirmationListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.BillingClient
import com.google.firebase.analytics.FirebaseAnalytics
import wallgram.hd.wallpapers.R
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import android.widget.Toast
import android.content.Intent
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.android.billingclient.api.BillingClientStateListener
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import com.google.android.material.textfield.TextInputLayout
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.local.preference.FIRST_LAUNCH
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.databinding.ActivityMainBinding
import wallgram.hd.wallpapers.ui.base.BaseActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import wallgram.hd.wallpapers.util.modo.*

import com.github.terrakok.cicerone.*
import wallgram.hd.wallpapers.App
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(ActivityMainBinding::inflate),
    ConfirmationListener {


    val modo = App.modo

    @Inject
    lateinit var preferences: PreferenceContract

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val modoRender by lazy {
        object : ModoRender(this@MainActivity, R.id.container) {

            override fun createMultiStackFragment(multiScreen: MultiScreen): MultiStackFragment =
                MainFragment()


            //only for sample
            override fun invoke(state: NavigationState) {
                super.invoke(state)

                if (state.chain.isEmpty())
                    return

                window.statusBarColor = ContextCompat.getColor(
                    this@MainActivity,
                    if (state.chain.last() is Screens.Wallpaper) R.color.color_status_bar else R.color.colorPrimaryDark
                )
                val stateStr =
                    "‣root\n${getNavigationStateString("⦙  ", state).trimEnd()}  ᐊ current screen"
                Log.d("STATE", stateStr)
            }


            //copy-paste from LogReducer
            private fun getNavigationStateString(
                prefix: String,
                navigationState: NavigationState
            ): String =
                navigationState.chain.map { screen ->
                    when (screen) {
                        is MultiScreen -> buildString {
                            append(prefix)
                            append('‣')
                            append(screen.id)
                            if (screen.stacks.size > 1) {
                                append(" [${screen.selectedStack + 1}/${screen.stacks.size}]")
                            }
                            append('\n')
                            append(
                                getNavigationStateString(
                                    "$prefix⦙  ",
                                    screen.stacks[screen.selectedStack]
                                )
                            )
                        }
                        else -> {
                            "$prefix${screen.id}\n"
                        }
                    }
                }.joinToString(separator = "")
        }
    }

    private fun exitFromApp() {
        if (back_pressed + 2000 > System.currentTimeMillis()) modo.exit() else Toast.makeText(
            baseContext, resources.getString(R.string.exit_toast),
            Toast.LENGTH_SHORT
        ).show()
        back_pressed = System.currentTimeMillis()
    }

    private fun showRateDialog() {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            ?: return
        val view = inflater.inflate(R.layout.rating_layout, findViewById(R.id.layout_root))
        val ratingBar = view.findViewById<RatingBar>(R.id.rating_bar)
        val feedbackText: TextInputLayout = view.findViewById(R.id.feedback_text)
        ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar1: RatingBar?, rating: Float, fromUser: Boolean ->
                feedbackText.visibility = if (rating <= 3.0f) View.VISIBLE else View.GONE
            }
        wallgram.hd.wallpapers.views.ratedialog.AppRate.with(this)
            .setView(view)
            .setCancelable(true)
            .setInstallDays(0)
            .setLaunchTimes(2)
            .setRemindInterval(2)
            .setShowLaterButton(true)
            .setDebug(false)
            .setOnClickButtonListener { which: Int ->
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    if (ratingBar.rating >= 4.0f) {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                        } catch (anfe: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                )
                            )
                        }
                    } else {
                        val emailIntent =
                            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "info@akspic.com"))
                        emailIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            resources.getString(R.string.email_subject)
                        )
                        emailIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            wallgram.hd.wallpapers.util.Common.getInfo(feedbackText.editText!!.text.toString())
                        )
                        try {
                            startActivity(
                                Intent.createChooser(
                                    emailIntent,
                                    resources.getString(R.string.send_feedback)
                                )
                            )
                        } catch (ex: ActivityNotFoundException) {
                            Toast.makeText(this, R.string.email_error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        wallgram.hd.wallpapers.views.ratedialog.AppRate.showRateDialogIfMeetsConditions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        showRateDialog()


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
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

        modo.init(
            savedInstanceState,
            if (preferences.getBoolean(
                    FIRST_LAUNCH,
                    true
                )
            ) Screens.Start() else Screens.MultiStack()
        )
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

    override fun onBackPressed() {

        val screen = (modoRender.currentState.chain[modoRender.currentState.chain.lastIndex])
        if(screen is MultiScreen){
            if(screen.stacks[screen.selectedStack].chain.size == 1)
                if(screen.selectedStack != 0){
                    modo.selectStack(0)
                } else exitFromApp()
            else modo.back()
        } else modo.back()
    }

    //override fun onSearchClick(query: String) {
//        val feedFragment = supportFragmentManager.findFragmentByTag("SEARCH") as FeedFragment?
//        feedFragment?.searchClicked(query)
    //   }

    override fun confirmButtonClicked() {
//        val feedFragment = supportFragmentManager.findFragmentByTag("FEED") as FeedFragment?
//        feedFragment?.confirmButtonClicked()
    }

    companion object {
        private var back_pressed: Long = 0
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java
}