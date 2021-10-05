package wallgram.hd.wallpapers

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.model.SubCategory
import wallgram.hd.wallpapers.ui.categories.CategoriesFragment
import wallgram.hd.wallpapers.ui.categories.CategoriesListFragment
import wallgram.hd.wallpapers.ui.components.crop.CropFragment
import wallgram.hd.wallpapers.ui.favorite.FavoriteFragment
import wallgram.hd.wallpapers.ui.main.HomeFragment
import wallgram.hd.wallpapers.ui.main.MainFragment
import wallgram.hd.wallpapers.ui.settings.SettingsFragment
import wallgram.hd.wallpapers.ui.search.SearchFragment
import wallgram.hd.wallpapers.ui.start.StartFragment
import wallgram.hd.wallpapers.ui.subscribe.SubscriptionFragment
import wallgram.hd.wallpapers.ui.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.AppScreen
import wallgram.hd.wallpapers.util.modo.ExternalScreen
import wallgram.hd.wallpapers.util.modo.MultiAppScreen
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.favorite.container.FavoriteContainerFragment
import wallgram.hd.wallpapers.ui.settings.language.LanguageFragment

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
    class Favorite : AppScreen("Favorite") {
        override fun create(): Fragment = FavoriteFragment.create(WallType.FAVORITE)
    }

    @Parcelize
    class History : AppScreen("History") {
        override fun create(): Fragment = FavoriteFragment.create(WallType.HISTORY)
    }

    @Parcelize
    class Subscription : AppScreen("Subs") {
        override fun create(): Fragment = SubscriptionFragment()
    }

    @Parcelize
    class Settings : AppScreen("Settings") {
        override fun create(): Fragment = SettingsFragment()
    }

    @Parcelize
    class Favorites: AppScreen("Favorites"){
        override fun create() = FavoriteContainerFragment.create()
    }

    @Parcelize
    class Wallpaper(private val position: Int,
                    private val pic: Int,
                    private val type: WallType = WallType.ALL) : AppScreen(pic.toString(), replacePreviousScreen = true) {
        override fun create(): Fragment = WallpaperFragment.create(type, position, pic)
    }

    @Parcelize
    class CategoriesList(private val feedRequest: @RawValue FeedRequest): AppScreen("CategoriesList"){
        override fun create() = CategoriesListFragment.create(feedRequest)
    }

    @Parcelize
    class Crop(private val landscape: String): AppScreen("Crop"){
        override fun create(): Fragment = CropFragment.create(landscape)
    }

    @Parcelize
    class Language: AppScreen("Language"){
        override fun create() = LanguageFragment.create()
    }

    fun MultiStack() = MultiAppScreen(
            "MultiStack",
            listOf(Home(), Categories(), Favorites(), Search(), Settings()),
            0
    )

    fun Browser(url: String) = ExternalScreen {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
//    @Parcelize
//    class Commands(private val i: Int) : AppScreen(i.toString()) {
//        override fun create() = CommandsFragment.create(i)
//    }
}