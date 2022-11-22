package wallgram.hd.wallpapers.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.data.IsSubscribed
import wallgram.hd.wallpapers.data.SubscriptionsDataSource
import wallgram.hd.wallpapers.data.ads.banner.BannerAd
import wallgram.hd.wallpapers.data.ads.interstitial.InterstitialAd

@Module
@InstallIn(ActivityComponent::class)
class AdsModule {

    @Provides
    @ActivityScoped
    fun provideAdInterstitial(
        @ActivityContext context: Context,
        subscriptionsDataSource: SubscriptionsDataSource
    ): InterstitialAd =
        InterstitialAd.Base(context, subscriptionsDataSource)




}