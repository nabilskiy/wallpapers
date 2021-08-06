package wallgram.hd.wallpapers.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import wallgram.hd.wallpapers.App
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import wallgram.hd.wallpapers.PREFERENCES
import wallgram.hd.wallpapers.data.local.Database
import wallgram.hd.wallpapers.data.local.Database.Companion.DATABASE_NAME
import wallgram.hd.wallpapers.data.local.dao.GalleryDao
import wallgram.hd.wallpapers.data.local.dao.SubscriptionStatusDao
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.data.local.preference.SharedPreferencesImpl
import wallgram.hd.wallpapers.data.repository.billing.LocalDataSource
import wallgram.hd.wallpapers.util.Network
import wallgram.hd.wallpapers.util.NetworkConnectivity
import wallgram.hd.wallpapers.util.billing.BillingClientLifecycle
import wallgram.hd.wallpapers.util.cache.CacheManager
import wallgram.hd.wallpapers.util.cache.ICacheManager
import java.util.concurrent.Executors

import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO

    @Provides
    @Singleton
    fun provideNetworkConnectivity(): NetworkConnectivity = Network(App.context)

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
            wallgram.hd.wallpapers.App.context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferences(): PreferenceContract = SharedPreferencesImpl(App.context)

    @Provides
    @Singleton
    fun provideCacheManager(): ICacheManager = CacheManager()

    @Provides
    @Singleton
    fun provideBillingClientLifecycle(): BillingClientLifecycle = BillingClientLifecycle.getInstance(wallgram.hd.wallpapers.App.context)

    @Singleton
    @Provides
    fun provideRequestManager(
            application: Application
    ): RequestManager {
        return Glide.with(application)
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): Database {
        return Room.databaseBuilder(application, Database::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .setQueryExecutor(Executors.newCachedThreadPool())
                .build()
    }

    @Provides
    @Singleton
    fun provideGalleryDao(database: Database): GalleryDao = database.galleryDao()

    @Provides
    @Singleton
    fun provideLocalDataSource(database: Database): LocalDataSource = LocalDataSource(database)

    @Provides
    @Singleton
    fun provideSubscriptionStatusDao(database: Database): SubscriptionStatusDao = database.subscriptionStatusDao()

}