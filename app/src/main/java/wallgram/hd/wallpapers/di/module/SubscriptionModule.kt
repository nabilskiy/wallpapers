package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.core.HandleError
import wallgram.hd.wallpapers.data.SubscriptionsDataSource
import wallgram.hd.wallpapers.data.favorites.FavoriteWallpapers
import wallgram.hd.wallpapers.data.favorites.FavoritesCacheDataSource
import wallgram.hd.wallpapers.data.favorites.FavoritesDao
import wallgram.hd.wallpapers.presentation.favorite.ChangeFavorite
import wallgram.hd.wallpapers.presentation.favorite.UpdateFavorites
import wallgram.hd.wallpapers.presentation.subscribe.ChangeSubscription
import wallgram.hd.wallpapers.presentation.subscribe.UpdateSubscriptions
import wallgram.hd.wallpapers.presentation.subscribe.buy.BuySubscriptions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SubscriptionModule {

    @Provides
    @Singleton
    fun provideChangeSubscription(cacheDataSource: SubscriptionsDataSource, update: UpdateSubscriptions.Update): ChangeSubscription =
        ChangeSubscription.Combo(cacheDataSource, update)

    @Provides
    @Singleton
    fun provideSubscriptionsDataSource(
    ): SubscriptionsDataSource =
        SubscriptionsDataSource.Base()

    @Provides
    @Singleton
    fun provideUpdateSubscriptionsObserve(updateSubscriptions: UpdateSubscriptions.Base): UpdateSubscriptions.Observe =
        updateSubscriptions

    @Provides
    @Singleton
    fun provideUpdateSubscriptionsUpdate(updateSubscriptions: UpdateSubscriptions.Base): UpdateSubscriptions.Update =
        updateSubscriptions

    @Provides
    @Singleton
    fun provideUpdateSubscriptions(): UpdateSubscriptions.Base = UpdateSubscriptions.Base()


    //BUY
    @Provides
    @Singleton
    fun provideBuySubscriptionsObserve(buySubscriptions: BuySubscriptions.Base): BuySubscriptions.Observe =
        buySubscriptions

    @Provides
    @Singleton
    fun provideBuySubscriptionsUpdate(buySubscriptions: BuySubscriptions.Base): BuySubscriptions.Update =
        buySubscriptions

    @Provides
    @Singleton
    fun provideBuySubscriptions(): BuySubscriptions.Base = BuySubscriptions.Base()


}