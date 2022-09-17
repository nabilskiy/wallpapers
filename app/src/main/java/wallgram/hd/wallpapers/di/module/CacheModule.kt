package wallgram.hd.wallpapers.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.data.gallery.WallpapersCache
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheModule {

    @Provides
    @Singleton
    fun provideWallpapersCache(): WallpapersCache.Mutable = WallpapersCache.Base()

    @Provides
    @Singleton
    fun provideSaveWallpapers(wallpapersCache: WallpapersCache.Mutable): WallpapersCache.Save =
        wallpapersCache

    @Provides
    @Singleton
    fun provideReadWallpapers(wallpapersCache: WallpapersCache.Mutable): WallpapersCache.Read =
        wallpapersCache

}