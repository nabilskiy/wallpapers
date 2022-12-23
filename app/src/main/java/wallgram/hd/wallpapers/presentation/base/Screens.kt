package wallgram.hd.wallpapers.presentation.base

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import wallgram.hd.wallpapers.presentation.categories.CategoriesFragment
import wallgram.hd.wallpapers.presentation.feed.FeedsFragment
import wallgram.hd.wallpapers.presentation.favorite.FavoriteFragment
import wallgram.hd.wallpapers.presentation.home.HomeFragment
import wallgram.hd.wallpapers.presentation.main.MainFragment
import wallgram.hd.wallpapers.presentation.settings.SettingsFragment
import wallgram.hd.wallpapers.presentation.search.SearchFragment
import wallgram.hd.wallpapers.presentation.start.StartFragment
import wallgram.hd.wallpapers.presentation.subscribe.SubscriptionFragment
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.util.modo.AppScreen
import wallgram.hd.wallpapers.util.modo.ExternalScreen
import wallgram.hd.wallpapers.util.modo.MultiAppScreen
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.presentation.changer.ChangerFragment
import wallgram.hd.wallpapers.presentation.history.HistoryFragment
import wallgram.hd.wallpapers.presentation.settings.language.LanguageFragment
import wallgram.hd.wallpapers.presentation.settings.resolution.ResolutionFragment
import wallgram.hd.wallpapers.presentation.wallpapers.WallpapersFragment
import kotlin.random.Random

object Screens {

    @Parcelize
    class Main : AppScreen("Main") {
        override fun create(): Fragment = MainFragment()
    }

    @Parcelize
    class Start : AppScreen("Start") {
        override fun create(): Fragment = StartFragment()
    }

    @Parcelize
    class Home : AppScreen("Home") {
        override fun create(): Fragment = HomeFragment()
    }

    @Parcelize
    class Categories : AppScreen("Categories") {
        override fun create(): Fragment = CategoriesFragment()
    }

    @Parcelize
    class Search : AppScreen("Search") {
        override fun create(): Fragment = SearchFragment()
    }

    @Parcelize
    class Subscription : AppScreen("Subs") {
        override fun create(): Fragment = SubscriptionFragment()
    }

    @Parcelize
    class Changer : AppScreen("Changer") {
        override fun create(): Fragment = ChangerFragment()
    }

    @Parcelize
    class Settings : AppScreen("Settings") {
        override fun create(): Fragment = SettingsFragment()
    }

    @Parcelize
    class Favorites : AppScreen("Favorites") {
        override fun create() = FavoriteFragment()
    }

    @Parcelize
    class History : AppScreen("History") {
        override fun create() = HistoryFragment()
    }

    @Parcelize
    class Wallpaper(private val screenId: Int) : AppScreen("Wallpaper_$screenId") {
        override fun create(): Fragment = WallpaperFragment.newInstance()
    }

    @Parcelize
    class CategoriesList(private val wallpaperRequest: @RawValue wallgram.hd.wallpapers.WallpaperRequest) :
        AppScreen("CategoriesList") {
        override fun create() = FeedsFragment.create(wallpaperRequest)
    }

    @Parcelize
    class Language : AppScreen("Language") {
        override fun create() = LanguageFragment()
    }

    @Parcelize
    class Resolution : AppScreen("Resolution") {
        override fun create() = ResolutionFragment()
    }

    @Parcelize
    class Wallpapers : AppScreen("Wallpapers") {
        override fun create() = WallpapersFragment()
    }

    fun MultiStack() = MultiAppScreen(
        "MultiStack",
        listOf(Home(), Categories(), Favorites(), Settings()),
        0
    )

    fun Browser(url: String) = ExternalScreen {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
}