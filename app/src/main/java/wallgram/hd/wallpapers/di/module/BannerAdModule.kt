package wallgram.hd.wallpapers.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.SubscriptionsDataSource
import wallgram.hd.wallpapers.data.ads.banner.BannerAd
import wallgram.hd.wallpapers.data.ads.recyclerbanner.RecyclerBannerAd

@Module
@InstallIn(FragmentComponent::class)
class BannerAdModule {

    @Provides
    @FragmentScoped
    fun provideAdBanner(
        context: Context,
        displayProvider: DisplayProvider,
        subscriptionsDataSource: SubscriptionsDataSource
    ): BannerAd = BannerAd.Base(context, displayProvider, subscriptionsDataSource)


}