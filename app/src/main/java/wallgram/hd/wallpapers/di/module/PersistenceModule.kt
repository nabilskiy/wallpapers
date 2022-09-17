package wallgram.hd.wallpapers.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import wallgram.hd.wallpapers.data.favorites.FavoritesDao
import wallgram.hd.wallpapers.data.local.AppDatabase
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFavoritesDao(database: AppDatabase): FavoritesDao = database.favoritesDao()

}
