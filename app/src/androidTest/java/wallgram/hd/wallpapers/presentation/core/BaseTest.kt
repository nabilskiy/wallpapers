package wallgram.hd.wallpapers.presentation.core

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import wallgram.hd.wallpapers.presentation.main.MainActivity

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
abstract class BaseTest {

    @get:Rule
    val activityTestRule = lazyActivityScenarioRule<MainActivity>(launchActivity = false)

    private lateinit var appContext: Context

    @Before
    open fun setup(){
        appContext = ApplicationProvider.getApplicationContext()

        activityTestRule.launch(Intent(appContext, MainActivity::class.java))
    }


}