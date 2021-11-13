package wallgram.hd.wallpapers.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import wallgram.hd.wallpapers.di.ViewModelKey
import wallgram.hd.wallpapers.ui.feed.FeedViewModel
import wallgram.hd.wallpapers.ui.base.ViewModelFactory
import wallgram.hd.wallpapers.ui.categories.CategoriesViewModel
import wallgram.hd.wallpapers.ui.crop.CropViewModel
import wallgram.hd.wallpapers.ui.dialogs.DownloadViewModel
import wallgram.hd.wallpapers.ui.favorite.FavoriteViewModel
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.ui.search.SearchViewModel
import wallgram.hd.wallpapers.ui.settings.SettingsViewModel
import wallgram.hd.wallpapers.ui.start.StartViewModel
import wallgram.hd.wallpapers.ui.subscribe.BillingViewModel
import wallgram.hd.wallpapers.ui.wallpaper.WallpaperViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    internal abstract fun bindCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartViewModel::class)
    internal abstract fun bindStartViewModel(viewModel: StartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DownloadViewModel::class)
    internal abstract fun bindDownloadViewModel(viewModel: DownloadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    internal abstract fun bindFeedViewModel(viewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WallpapersViewModel::class)
    internal abstract fun bindWallpapersViewModel(viewModel: WallpapersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    internal abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BillingViewModel::class)
    internal abstract fun bindBillingViewModel(viewModel: BillingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CropViewModel::class)
    internal abstract fun bindCropViewModel(viewModel: CropViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WallpaperViewModel::class)
    internal abstract fun bindWallpaperViewModel(viewModel: WallpaperViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}