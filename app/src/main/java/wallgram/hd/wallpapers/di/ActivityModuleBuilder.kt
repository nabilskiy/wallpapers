package wallgram.hd.wallpapers.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import wallgram.hd.wallpapers.ui.categories.CategoriesFragment
import wallgram.hd.wallpapers.ui.feed.FeedsFragment
import wallgram.hd.wallpapers.ui.crop.CropFragment
import wallgram.hd.wallpapers.ui.dialogs.CropDialogFragment
import wallgram.hd.wallpapers.ui.dialogs.DownloadDialogFragment
import wallgram.hd.wallpapers.ui.dialogs.InstallDialogFragment
import wallgram.hd.wallpapers.ui.favorite.FavoriteFragment
import wallgram.hd.wallpapers.ui.favorite.container.FavoriteContainerFragment
import wallgram.hd.wallpapers.ui.home.HomeFragment
import wallgram.hd.wallpapers.ui.main.MainActivity
import wallgram.hd.wallpapers.ui.main.MainFragment
import wallgram.hd.wallpapers.ui.main.SplashActivity
import wallgram.hd.wallpapers.ui.search.SearchFragment
import wallgram.hd.wallpapers.ui.settings.SettingsFragment
import wallgram.hd.wallpapers.ui.settings.language.LanguageFragment
import wallgram.hd.wallpapers.ui.start.StartFragment
import wallgram.hd.wallpapers.ui.subscribe.SubscriptionFragment
import wallgram.hd.wallpapers.ui.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersFragment

@Suppress("unused")
@Module
abstract class ActivityModuleBuilder {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector()
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector()
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector()
    abstract fun contributeCategoriesFragment(): CategoriesFragment

    @ContributesAndroidInjector()
    abstract fun contributeCategoriesListFragment(): FeedsFragment

    @ContributesAndroidInjector()
    abstract fun contributeStartFragment(): StartFragment

    @ContributesAndroidInjector()
    abstract fun contributeWallpapersFragment(): WallpapersFragment

    @ContributesAndroidInjector()
    abstract fun contributeWallpaperFragment(): WallpaperFragment

    @ContributesAndroidInjector()
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector()
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector()
    abstract fun contributeSubscriptionFragment(): SubscriptionFragment

    @ContributesAndroidInjector()
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector()
    abstract fun contributeInstallDialogFragment(): InstallDialogFragment

    @ContributesAndroidInjector()
    abstract fun contributeDownloadDialogFragment(): DownloadDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeCropDialogFragment(): CropDialogFragment

    @ContributesAndroidInjector()
    abstract fun contributeCropFragment(): CropFragment

    @ContributesAndroidInjector()
    abstract fun contributeLanguageFragment(): LanguageFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteContainerFragment(): FavoriteContainerFragment

}